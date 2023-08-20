package com.example.familytodos.data.model

import com.google.firebase.Timestamp

data class Group(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val creatorId: String = "",
    val img: String? =  null,
    val img_description: String = "",
    val members: List<User> = emptyList(),
    val createdTimestamp : Timestamp = Timestamp.now()
)

