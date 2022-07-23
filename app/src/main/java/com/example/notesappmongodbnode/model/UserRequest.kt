package com.example.notesappmongodbnode.model

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)