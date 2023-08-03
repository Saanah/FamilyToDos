package com.example.familytodos.data

import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {

    val db: FirebaseFirestore
    val currentUser: FirebaseUser?
    suspend fun addUserToFirestore(username: String, email : String): Unit
    suspend fun getGroupById(groupId: String): Group
    suspend fun getUserGroups():  MutableList<Group>
    suspend fun createGroup(name: String, description: String):  String
    suspend fun findUser(name: String):  MutableList<User>
    suspend fun addSelectedUsersToGroup(groupId: String, selectedUsers: List<User>) : String
    suspend fun createTask(groupId: String, task: String, username: String?) : String
    suspend fun getGroupInformation(groupId: String) : Group
    //suspend fun createComment(): Comment

}