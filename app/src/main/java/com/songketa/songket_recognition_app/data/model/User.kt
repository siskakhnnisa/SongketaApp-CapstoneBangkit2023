package com.songketa.songket_recognition_app.data.model

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val token: String,
    val isLogin: Boolean = false
)
