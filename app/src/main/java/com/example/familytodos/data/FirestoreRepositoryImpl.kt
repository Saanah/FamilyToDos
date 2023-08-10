package com.example.familytodos.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.Task
import com.example.familytodos.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : FirestoreRepository {

    override val db: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun addUserToFirestore(username: String, email: String) {

        val userId = currentUser?.uid

        if (userId != null) {

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
                .addOnFailureListener { Log.d("error", "Not able to add user to Firestore") }
        }
    }


    override suspend fun getGroupById(groupId: String): Group {

        var group = Group()

        try {
            val querySnapshot = db.collection("Group").whereEqualTo("id", groupId).get().await()
            val groupDocument =
                querySnapshot.documents.firstOrNull() //Get the first document, return null if collection is empty
            group = groupDocument?.toObject(Group::class.java)
                ?: Group() //Turn into an object or return an empty object
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching group information", e)
        }

        return group
    }

    override suspend fun getUserGroups(): MutableList<Group> {

        val userId = currentUser?.uid
        var group: Group
        var groupList = mutableListOf<Group>()


        try {

            db.collection("Group")
                .whereEqualTo("creatorId", "$userId")
                .orderBy("name", Query.Direction.ASCENDING)
                .orderBy("createdTimestamp", Query.Direction.DESCENDING)
                .get().await().map {

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

        val userId = currentUser?.uid
        val documentRef = db.collection("Group").document() //Create a new Group document
        var documentId = documentRef.id                                //get the freshly created document's documentId

        //Create a hashmap of user's inputs and the needed ids
        val group = hashMapOf(
            "id" to documentId,
            "name" to name,
            "description" to description,
            "creatorId" to userId,
            "img" to null,
            "img_description" to "",
            "members" to emptyList<User>(),
            "createdTimestamp" to Timestamp.now()
        )

        try {
            //create the group in Firestore with the given information
            documentRef.set(group)
            Log.d(TAG, "Group successfully created!")

        } catch (e: Exception) {
            Log.e(TAG, "Error creating a group.", e)
        }

        return documentId
    }

    override suspend fun findUser(username: String): MutableList<User> {

        var user: User
        var userList = mutableListOf<User>()


        try {

            db.collection("Users").whereEqualTo("username", username).get().await().map {

                val user = it.toObject(User::class.java)

                userList.add(user)

            }
        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getDataFromFireStore $e")

        }

        return userList
    }

    override suspend fun addSelectedUsersToGroup(
        groupId: String,
        selectedUsers: List<User>
    ): String {

        return try {

            //Turn selectedUser information into a list of hashmaps
            val users = selectedUsers.map { user ->

                hashMapOf(
                    "userId" to user.userId,
                    "username" to user.username,
                    "email" to user.email,
                    "profile_img" to user.profile_img,
                    "profile_desc" to user.profile_desc

                )
            }

            // Update the group document with the selected users
            val groupDocumentRef = db.collection("Group").document(groupId)
            groupDocumentRef.update("members", FieldValue.arrayUnion(*users.toTypedArray()))

            "Users successfully added to the group."

        } catch (e: Exception) {
            "Error adding users to the group: ${e.message}"
        }
    }

    override suspend fun createTask(
        groupId: String,
        task: String,
        username: String,
        userId: String,
        isCompleted: Boolean
    ): String = suspendCoroutine { continuation -> //wrap async operation inside coroutine

        val taskDocumentRef = db.collection("Task").document() //Create task document
        var documentId = taskDocumentRef.id

        val task = hashMapOf(
            "id" to documentId,
            "groupId" to groupId,
            "task" to task,
            "username" to username,
            "userId" to userId,
            "isCompleted" to isCompleted
        )

        taskDocumentRef.set(task)
            .addOnSuccessListener {
                Log.d(TAG, "Task successfully created!")
                continuation.resume("Task succesfully created!") //resume function and return message
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Error creating a task", e)
                continuation.resume("Error creating a task: $e")
            }
    }

    override suspend fun getUserTasks(groupId: String): MutableList<Task> {

        val userId = currentUser?.uid
        var userTasks = mutableListOf<Task>()

        try {

            val querySnapshot = db.collection("Task")
                .whereEqualTo("userId", userId)
                .whereEqualTo("groupId", groupId).get().await()

            for (documentSnapshot in querySnapshot.documents) {

                val task = documentSnapshot.toObject(Task::class.java)

                if (task != null) {
                    val isCompleted = documentSnapshot.getBoolean("isCompleted") ?: false
                    task.isCompleted = isCompleted

                    userTasks.add(task)
                }
            }
        }
        catch (e: FirebaseFirestoreException) {
            Log.d("error", "getTaskDataFromFireStore $e")
        }

        return userTasks
    }

    override suspend fun getAllTasksFromGroup(groupId: String): MutableList<Task> {

        var userTasks = mutableListOf<Task>()

        try {

            db.collection("Task").whereEqualTo("groupId", groupId).get().await().map {

                val task = it.toObject(Task::class.java)
                userTasks.add(task)
            }

        }
        catch (e: FirebaseFirestoreException) {
            Log.d("error", "getTaskDataFromFireStore $e")
        }

        return userTasks
    }

    override suspend fun changeTaskStatus(taskId: String, isCompleted: Boolean) {

       val taskDocumentRef =  db.collection("Task").document(taskId)
        //Hashmap for the task, updates isCompleted status
        val updateTaskStatus = hashMapOf<String, Any>(
            "isCompleted" to isCompleted
        )

        //Update isCompleted field only
        taskDocumentRef.update(updateTaskStatus).await()
    }
}
