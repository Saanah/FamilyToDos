package com.example.familytodos.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirestoreRepository {

    override val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun addUserToFirestore(username: String, email : String) {

        val userId = currentUser?.uid

        if (userId != null){

            //Create a hashmap of user
            val user = hashMapOf(
                "userId" to userId,
                "username" to username,
                "email" to email,
                "profile_desc" to "",
                "profile_img" to ""
            )

            //add registered user to Firestore
            db.collection("Users").document(userId).set(user)
                .addOnSuccessListener { Log.d("success", "User added to Firestore") }
                .addOnFailureListener {  Log.d("error", "Not able to add user to Firestore") }
        }
    }


    override suspend fun getGroupById(groupId: String): Group {

        //val documentRef = db.collection("Group").document(groupId)
        var group = Group()

        try {

            db.collection("Group").whereEqualTo("id", "$groupId").get().await().map {

                val response = it.toObject(Group::class.java)
                group = response
            }

        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getDataFromFireStore $e")

        }

        return group
    }

    override suspend fun getUserGroups(): MutableList<Group> {

        val userId = currentUser?.uid
        var group: Group
        var groupList = mutableListOf<Group>()


        try {

            db.collection("Group").whereEqualTo("creatorId", "$userId").get().await().map {

                val response = it.toObject(Group::class.java)
                group = response
                groupList.add(group)
            }

        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getDataFromFireStore $e")

        }

        return groupList
    }

    override suspend fun createGroup(name: String, description: String): String {

        var result = ""
        var documentId = ""
        val userId = currentUser?.uid

        try {

            //Create a hashmap of user's inputs and the needed ids
            val group = hashMapOf(
                "id" to "",
                "name" to name,
                "description" to description,
                "creatorId" to userId
            )

            val documentRef = db.collection("Group").document() //Create a new Group document
            documentId = documentRef.id                                 //get the freshly created document's documentId
            group["id"] = documentId                                        //Set the document id into the hashmap

            //create the group in Firestore with the given information
            documentRef.set(group)
                .addOnSuccessListener {
                    Log.d(TAG, "Group successfully created!"); result = "Group created!"
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error creating a document.", e); result =
                    "Error creating a new group."
                }

        } catch (e: Exception) {

            result = "Something went wrong."
        }

        return documentId
    }

    override suspend fun findUser(username: String): MutableList<User> {

        var user: User
        var userList = mutableListOf<User>()


        try {

            db.collection("Users").whereEqualTo("username", username).get().await().map {

                val response = it.toObject(User::class.java)

                user = response
                userList.add(user)

            }
        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getDataFromFireStore $e")

        }

        return userList
    }

    override suspend fun addSelectedUsersToGroup( groupId: String, selectedUsers: List<User> ): String {

        return try {
            // turn into a list of user ids
            val userIds = selectedUsers.map { it.userId }

            // Update the group document with the selected user ids
            val groupRef = db.collection("Group").document(groupId)
            groupRef.update("members", FieldValue.arrayUnion(*userIds.toTypedArray()))

            "Users successfully added to the group."

        } catch (e: Exception) {
            "Error adding users to the group: ${e.message}"
        }

    }
}
