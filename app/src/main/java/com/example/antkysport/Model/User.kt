package com.example.antkysport.Model

data class UserResponse(
    val user: User // `user` đại diện cho object con chứa name và email
)

data class User(
    val name: String,
    val email: String
)