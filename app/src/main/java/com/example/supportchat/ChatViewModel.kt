package com.example.supportchat

import android.util.Log
import androidx.lifecycle.ViewModel

class ChatViewModel: ViewModel() {

    fun sendMessage(question : String){
        Log.i("In chatviewModel ", question)
    }
}