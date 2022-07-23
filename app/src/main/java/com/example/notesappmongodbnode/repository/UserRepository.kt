package com.example.notesappmongodbnode.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesappmongodbnode.api.UserAPI
import com.example.notesappmongodbnode.model.UserRequest
import com.example.notesappmongodbnode.model.UserResponse
import com.example.notesappmongodbnode.utils.Constants.TAG
import com.example.notesappmongodbnode.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(val userAPI: UserAPI) {

    private val _userResponseLivData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLivData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLivData.postValue(NetworkResult.Loading())
        val response = userAPI.signUp(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest){
        _userResponseLivData.postValue(NetworkResult.Loading())
        val response = userAPI.signIn(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLivData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLivData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLivData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}