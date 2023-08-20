package com.example.familytodos.data

import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.Points
import com.example.familytodos.data.model.Task
import com.example.familytodos.data.model.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {

    val db: FirebaseFirestore
    val currentUser: FirebaseUser?
    suspend fun addUserToFirestore(username: String, email : String): Unit
    suspend fun getUserInformation() : User
    suspend fun getGroupById(groupId: String): Group
    suspend fun getUserGroups():  MutableList<Group>
    suspend fun createGroup(name: String, description: String):  String
    suspend fun findUser(name: String):  MutableList<User>
    suspend fun addSelectedUsersToGroup(groupId: String, selectedUsers: MutableList<User>) : String
    suspend fun deleteMemberFromGroup(userId: String, groupId : String) : Unit
    suspend fun createTask(groupId: String, task: String, username: String, userId : String, isCompleted: Boolean, timestamp: Timestamp): String
    suspend fun getUserTasks(groupId: String): MutableList<Task>
    suspend fun getAllTasksFromGroup(groupId: String): MutableList<Task>
    suspend fun changeTaskStatus(taskId: String, isCompleted: Boolean)
    suspend fun addOrDeleteCompletedTaskPoints(groupId: String, username: String, points : Int)
    suspend fun getUserTaskPoints() : Points
    suspend fun getGroupTaskPointsPerUser(groupId: String) : MutableList<Points>
    suspend fun deleteTask(taskId : String) : Unit
    suspend fun updateUserProfileDescription(profile_desc : String) : Unit

}