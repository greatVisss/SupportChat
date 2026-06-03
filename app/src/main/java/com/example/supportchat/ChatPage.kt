package com.example.supportchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel){
    Column(modifier = modifier) {
        messageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
    }
}

@Composable
fun messageInput(onMessageSend : (String) -> Unit){
    var message by remember { mutableStateOf("") }

    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = message, onValueChange = { message = it},
            modifier = Modifier.weight(1f))
        IconButton(onClick = {
            onMessageSend(message)
            message = ""
        }) { Icon(imageVector = Icons.Default.Send, contentDescription = "Send") }
    }
}