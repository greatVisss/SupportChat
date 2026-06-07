package com.example.supportchat

import android.content.Context

object SessionManager {

    private const val PREF="session"

    private const val KEY_USER="username"

    fun saveUser(
        context: Context,
        username:String
    ){

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_USER,
                username
            )
            .apply()

    }

    fun getUser(
        context: Context
    ):String?{

        return context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .getString(
                KEY_USER,
                null
            )

    }

    fun logout(
        context: Context
    ){

        context
            .getSharedPreferences(
                PREF,
                Context.MODE_PRIVATE
            )
            .edit()
            .clear()
            .apply()

    }

}