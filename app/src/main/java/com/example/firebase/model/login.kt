package com.example.firebase

data class Login(
    val infoUser: InfoUser,
    val message: String,
    val success: Int
)

data class InfoUser(
    val diachi: String? = null,
    val fullname: String? = null,
    val id: String? = null,
    val password: String? =null,
    val pq: Int? = null,
    val sdt: String? = null,
    val email: String? =null
)