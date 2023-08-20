package com.example.familytodos.data

import android.content.ContentValues.TAG
import android.util.Log
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.Points
import com.example.familytodos.data.model.Task
import com.example.familytodos.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

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
                "profile_desc" to "I'm a hard worker in FamilyToDos!",
                "profile_img" to ""
            )

            //add registered user to Firestore
            db.collection("Users").document(userId).set(user)
                .addOnSuccessListener { Log.d("success", "User added to Firestore") }
                .addOnFailureListener { Log.d("error", "Not able to add user to Firestore") }
        }
    }

    override suspend fun getUserInformation(): User {

        val userId = currentUser?.uid
        var user = User()

        try {
            if (userId != null) {
                val userDocument = db.collection("Users").document(userId).get().await()
                if (userDocument.exists()) {
                    user = userDocument.toObject(User::class.java) ?: User()
                }
            }
        }
        catch (e: FirebaseFirestoreException){

            Log.d(TAG, "Error fetching group information ${e.message}")
        }

        return user
    }

    override suspend fun getGroupById(groupId: String): Group {

        var group = Group()

        try {
            val querySnapshot = db.collection("Group").whereEqualTo("id", groupId).get().await()
            val groupDocument = querySnapshot.documents.firstOrNull() //Get the first document, return null if collection is empty
            group = groupDocument?.toObject(Group::class.java) ?: Group() //Turn into an object or return an empty object
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching group information ${e.message}")
        }

        return group
    }

    override suspend fun getUserGroups(): MutableList<Group> {
        val groupList = mutableListOf<Group>()

        try {
            val userId = currentUser?.uid
            if (userId != null) {
                // Find the groups user has created
                val creatorGroupsQuery = db.collection("Group")
                    .whereEqualTo("creatorId", userId)
                    .orderBy("name", Query.Direction.ASCENDING)
                    .orderBy("createdTimestamp", Query.Direction.DESCENDING)
                    .get().await()

                val creatorGroups = creatorGroupsQuery.documents.mapNotNull { document ->
                    document.toObject(Group::class.java)
                }

                groupList.addAll(creatorGroups)

                // Find the groups the user is a member of
                val userGroupsQuery = db.collection("UserGroups")
                    .whereEqualTo("userId", userId)
                    .get().await()

                if (!userGroupsQuery.isEmpty) { // Check if there are any documents in the query result

                    // Get user's groupIds
                    val groupIds = userGroupsQuery.documents.mapNotNull { document ->
                        document.getString("groupId")
                    }

                    // Get groups where the groupIds match
                    val memberGroupsQuery = db.collection("Group")
                        .whereIn("id", groupIds)
                        .orderBy("name")
                        .get().await()

                    // Turn into a list of objects
                    val memberGroups = memberGroupsQuery.documents.mapNotNull { document ->
                        document.toObject(Group::class.java)
                    }

                    groupList.addAll(memberGroups)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "getDataFromFireStore ${e.message}")
        }

        return groupList
    }


    override suspend fun createGroup(name: String, description: String): String {

        val userId = currentUser?.uid
        val documentRef = db.collection("Group").document() //Create a new Group document
        var documentId =
            documentRef.id                                //get the freshly created document's documentId

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

        var userList = mutableListOf<User>()


        try {
            val userId = currentUser?.uid

            if (currentUser != null) {
                //Search for users excluding the current user
                db.collection("Users").whereEqualTo("username", username)
                    .whereNotEqualTo(FieldPath.documentId(), userId).get().await().map {

                        val user = it.toObject(User::class.java)

                        userList.add(user)

                    }
            }
        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getDataFromFireStore $e")

        }

        return userList
    }

    override suspend fun addSelectedUsersToGroup(
        groupId: String,
        selectedUsers: MutableList<User>
    ): String {

        val currentUserId = currentUser?.uid

        // Add the users to a group in Firestore
        return try {

            //Get the current user / group creator from Firestore
            val user = currentUserId?.let {
                db.collection("Users").document(it).get().await()?.toObject(User::class.java)
            }

            val usersWithCreator = selectedUsers + user

            // Turn user information into a list of hashmaps
            val users = usersWithCreator.map { user ->
                hashMapOf(
                    "userId" to user?.userId,
                    "username" to user?.username,
                    "email" to user?.email,
                    "profile_img" to user?.profile_img,
                    "profile_desc" to user?.profile_desc
                )
            }

            // Update the group document with the selected users
            val groupDocumentRef = db.collection("Group").document(groupId)
            groupDocumentRef.update("members", FieldValue.arrayUnion(*users.toTypedArray()))

            // Create a new document in the UserGroups collection for each member
            selectedUsers.forEach { memberUser ->
                memberUser?.userId?.let { userId ->
                    val userGroupsDocumentRef = db.collection("UserGroups")
                    val userGroupData = hashMapOf(
                        "userId" to userId,
                        "groupId" to groupId,
                        "username" to memberUser.username
                    )
                    //Update the created document with user group information
                    userGroupsDocumentRef.add(userGroupData)
                }
            }

            "Users successfully added to the group."

        } catch (e: Exception) {
            "Error adding users to the group: ${e.message}"
        }
    }

    override suspend fun deleteMemberFromGroup(userId: String, groupId: String) {
        try {
            // Fetch the needed group document
            val groupDocRef = db.collection("Group").document(groupId)
            val groupSnapshot = groupDocRef.get().await()

            // Get the current members array with user objects
            val currentMembers = groupSnapshot.get("members") as? List<Map<String, Any>>

            if (currentMembers != null) {
                // Filter out the user object with the matching userId to delete the user
                val updatedMembers = currentMembers.filter { member ->
                    member["userId"] != userId
                }

                // Update the document members field with the filtered members array
                val updateMembersData = hashMapOf<String, Any>("members" to updatedMembers)
                groupDocRef.update(updateMembersData).await()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "deleteMemberFromGroup ${e.message}")
        }
    }

    override suspend fun createTask(
        groupId: String,
        task: String,
        username: String,
        userId: String,
        isCompleted: Boolean,
        timestamp: Timestamp
    ): String = try {
        val taskDocumentRef = db.collection("Task").document() // Create task document
        val documentId = taskDocumentRef.id

        val taskData = hashMapOf(
            "id" to documentId,
            "groupId" to groupId,
            "task" to task,
            "username" to username,
            "userId" to userId,
            "isCompleted" to isCompleted,
            "timestamp" to timestamp
        )

        taskDocumentRef.set(taskData).await() // Use `await` to await the result of the asynchronous call
        Log.d(TAG, "Task successfully created!")
        "Task successfully created!"
    } catch (e: Exception) {
        Log.d(TAG, "Error creating a task", e)
        "Error creating a task: ${e.message}"
    }


    override suspend fun getUserTasks(groupId: String): MutableList<Task> {

        val userId = currentUser?.uid
        var userTasks = mutableListOf<Task>()

        try {

            //Get the current date and time
            val calendar = Calendar.getInstance()

            //Set the time to the start of the day
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = Timestamp(calendar.time)

            //Set the time to the end of the day
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = Timestamp(calendar.time)


            val querySnapshot = db.collection("Task")
                .whereEqualTo("userId", userId)
                .whereEqualTo("groupId", groupId)
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThanOrEqualTo("timestamp", endOfDay)
                .get().await()

            for (documentSnapshot in querySnapshot.documents) {

                val task = documentSnapshot.toObject(Task::class.java)

                if (task != null) {
                    val isCompleted = documentSnapshot.getBoolean("isCompleted") ?: false
                    task.isCompleted = isCompleted

                    userTasks.add(task)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "getTaskDataFromFirestore ${e.message}")
        }

        return userTasks
    }

    override suspend fun getAllTasksFromGroup(groupId: String): MutableList<Task> {

        var allTasks = mutableListOf<Task>()

        try {

            //Get the current date and time
            val calendar = Calendar.getInstance()

            //Set the time to the start of the day
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = Timestamp(calendar.time)

            //Set the time to the end of the day
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
            val endOfDay = Timestamp(calendar.time)

            val querySnapshot = db.collection("Task")
                .whereEqualTo("groupId", groupId)
                .whereGreaterThanOrEqualTo("timestamp", startOfDay)
                .whereLessThanOrEqualTo("timestamp", endOfDay).get().await()

            for (documentSnapshot in querySnapshot.documents) {

                val task = documentSnapshot.toObject(Task::class.java)

                if (task != null) {
                    val isCompleted = documentSnapshot.getBoolean("isCompleted") ?: false
                    task.isCompleted = isCompleted

                    allTasks.add(task)
                }
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d("error", "getFamilyTaskDataFromFirestore $e")
        }

        return allTasks
    }

    override suspend fun changeTaskStatus(taskId: String, isCompleted: Boolean) {

        val taskDocumentRef = db.collection("Task").document(taskId)
        //Hashmap for the task, updates isCompleted status
        val updateTaskStatus = hashMapOf<String, Any>(
            "isCompleted" to isCompleted
        )

        //Update isCompleted field only
        taskDocumentRef.update(updateTaskStatus).await()
    }

    override suspend fun deleteTask(taskId: String) {

        try {
            db.collection("Task").document(taskId).delete()
        }
        catch (e: FirebaseFirestoreException){
            Log.d("error", "deleteTaskFromFirestore ${e.message}")
        }
    }

    override suspend fun updateUserProfileDescription(profile_desc: String) {

        val userId = currentUser?.uid

        try{
            if (userId != null) {
                db.collection("Users").document(userId).update("profile_desc", profile_desc)
            }
        }
        catch (e: FirebaseFirestoreException){
            Log.d("error", "updateUserProfileDesc ${e.message}")
        }

    }

    override suspend fun addOrDeleteCompletedTaskPoints(
        groupId: String,
        username: String,
        points: Int
    ) {

        val userId = currentUser?.uid

        try {
            //Check if user is already in the points collection
            val doesUserPointsExistQuery =
                db.collection("Points").whereEqualTo("userId", userId).whereEqualTo("groupId", groupId).get().await()

            Log.d("Koira", "${doesUserPointsExistQuery.documents}")

            if (doesUserPointsExistQuery.isEmpty) {
                // If user doesn't exist in the points collection, add a new document and points
                val pointsData = hashMapOf(
                    "userId" to userId,
                    "groupId" to groupId,
                    "username" to username,
                    "totalPoints" to points
                )

                db.collection("Points").add(pointsData).await()

            } else {
                // If user already exists, update their existing document and add points
                val userPointsDocument = doesUserPointsExistQuery.documents.first()
                val existingTotalPoints = userPointsDocument.getLong("totalPoints") ?: 0
                val newTotalPoints = existingTotalPoints + points

                userPointsDocument.reference.update("totalPoints", newTotalPoints).await()
            }
        }
        catch (e: FirebaseFirestoreException){

            Log.e("error", "Error adding points: ${e.message}")
        }
    }
    override suspend fun getUserTaskPoints() : Points {

        val userId = currentUser?.uid

        try {
            val userPointsQuery =
                db.collection("Points").whereEqualTo("userId", userId).get().await()

            if (userPointsQuery != null) {
                val pointsDocument = userPointsQuery.documents.first()
                return pointsDocument.toObject(Points::class.java) ?: Points()
            }
        } catch (e: FirebaseFirestoreException) {

            Log.d("error", "getUserPointsFromFirestore ${e.message}")
        }

        return Points()
    }

    override suspend fun getGroupTaskPointsPerUser(groupId: String): MutableList<Points> {

        var pointsList = mutableListOf<Points>()

        try {

            db.collection("Points").whereEqualTo("groupId", groupId).orderBy("totalPoints", Query.Direction.DESCENDING).get().await().map {

                val points = it.toObject(Points::class.java)

                pointsList.add(points)

            }
        }
        catch (e : FirebaseFirestoreException)
        {
            Log.d("error", "getGroupTaskPointsPerUserFromFirestore ${e.message}")

        }


        return pointsList
    }
}

