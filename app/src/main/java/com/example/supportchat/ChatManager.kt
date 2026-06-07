package com.example.supportchat

import com.google.firebase.firestore.FirebaseFirestore

object ChatManager {

    private val db=
        FirebaseFirestore
            .getInstance()

    fun createChat(
        username:String,
        chatId:String,
        title:String
    ){
        val data=
            hashMapOf(
                "id" to chatId,
                "title" to title,
                "timestamp"
                        to
                        System.currentTimeMillis()

            )

        db.collection("userschats")
            .document(username)
            .collection("chats")
            .document(chatId)
            .set(data)
    }

    fun saveMessage(
        username:String,
        chatId:String,
        message:MessageModel
    ){

        db.collection("userschats")
            .document(username)
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)

    }

}