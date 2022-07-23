package com.example.notesappmongodbnode.model

data class UserResponse(
    val token: String,
    val user: User
)