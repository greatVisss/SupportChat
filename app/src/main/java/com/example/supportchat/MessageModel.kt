package com.example.supportchat

data class MessageModel(
    val message : String="",
    val role : String="",

    val timestamp: Long = System.currentTimeMillis()
)
