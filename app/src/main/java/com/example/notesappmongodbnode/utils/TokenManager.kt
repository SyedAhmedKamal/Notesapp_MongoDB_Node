package com.example.notesappmongodbnode.utils

import android.content.Context
import com.example.notesappmongodbnode.utils.Constants.PREFS_FILE
import com.example.notesappmongodbnode.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context:Context) {

    private var prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    fun saveToken(token:String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken() = prefs.getString(USER_TOKEN, null)

}