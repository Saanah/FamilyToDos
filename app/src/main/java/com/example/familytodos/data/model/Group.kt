package com.example.familytodos.data.model

data class Group(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val creatorId: String = "",
    val img: String? =  null,
    val img_description: String = "",
    val members: List<User> = emptyList()
)
