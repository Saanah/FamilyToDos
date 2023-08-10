package com.example.familytodos.data.model

data class Task(
    val id: String = "",
    val groupId: String = "",
    val userId: String = "",
    val task: String = "",
    val username: String = "",
    var isCompleted: Boolean = false
)
