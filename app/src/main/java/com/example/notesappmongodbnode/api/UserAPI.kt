package com.example.notesappmongodbnode.api

import com.example.notesappmongodbnode.model.UserRequest
import com.example.notesappmongodbnode.model.UserResponse
import com.example.notesappmongodbnode.utils.Constants.SIGN_IN
import com.example.notesappmongodbnode.utils.Constants.SIGN_UP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST(SIGN_UP)
    suspend fun signUp(@Body userRequest: UserRequest): Response<UserResponse>

    @POST(SIGN_IN)
    suspend fun signIn(@Body userRequest: UserRequest): Response<UserResponse>

}