package com.example.supportchat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supportchat.ui.theme.ColorModelMessage
import com.example.supportchat.ui.theme.ColorUserMessage
import org.intellij.lang.annotations.JdkConstants

@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
){
    val context = LocalContext.current
    var sidebarOpen by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()
    ){
        Column{
            MenuRow(
                username = viewModel.currentUser,
                onOpenMenu = { sidebarOpen = !sidebarOpen },
                onLogout = { SessionManager.logout(context)
                    viewModel.messageList.clear()
                    (context as MainActivity).recreate()
                }
            )

            MessageList(
                messageList = viewModel.messageList,
                modifier = Modifier.weight(1f)
            )

            messageInput{ viewModel.sendMessage(it) }
        }

        if(sidebarOpen){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = .35f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){
                        sidebarOpen = false
                    }
            ){
                Column(modifier = Modifier.fillMaxHeight()
                    .size(width = 190.dp, height = 0.dp)
                    .background(Color.DarkGray).align(Alignment.CenterStart)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }){ /* consume click */}
                ){
                    TextButton(onClick = {
                            sidebarOpen = false
                            viewModel.createNewChat()
                        }
                    ){
                        Text("+ Nuevo Chat")
                    }
                    LazyColumn{
                        items(viewModel.chatList){
                            TextButton(
                                onClick = {
                                    sidebarOpen = false
                                    viewModel.loadChat(it.id)
                                }
                            ){
                                Text(it.title)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>){
    if (messageList.isEmpty()){
        Column(modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Image(modifier = Modifier.size(110.dp),
                painter = painterResource(id = R.drawable.chatbot_icon),
                contentDescription = "Icon"
            )
            Text(text = "¿Como te sientes hoy?",
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.Gray)
        }
    }else {
        LazyColumn(modifier = modifier, reverseLayout = true) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}

@Composable
fun MessageRow(messageModel: MessageModel){
    val isModel = messageModel.role == "model"
    
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.fillMaxWidth()){
            Box(modifier = Modifier
                .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                .padding(
                    start = if (isModel) 10.dp else 65.dp,
                    end = if (isModel) 65.dp else 10.dp,
                    top = 5.dp, bottom = 5.dp
                )
                .clip(RoundedCornerShape(30.dp))
                .background(if (isModel) ColorModelMessage else ColorUserMessage)
                .padding(18.dp)) {
                    Text(text = messageModel.message, fontWeight = FontWeight.W400,
                        color = if(isModel) Color.White else Color.Black)
            }
        }
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
            if(message.isNotEmpty()) {
                onMessageSend(message)
                message = ""
            }
        }) { Icon(imageVector = Icons.Default.Send,
            contentDescription = "Send",
            tint = Color.Blue,
            modifier = Modifier.size(50.dp)) }
    }
}

@Composable
fun MenuRow(
    username:String,
    onOpenMenu:()->Unit,
    onLogout:()->Unit
){
    Row(
        modifier=
            Modifier
                .fillMaxWidth()
                .background(
                    Color.Blue
                ),
        verticalAlignment=
            Alignment.CenterVertically
    ){
        TextButton(onClick= onOpenMenu
        ){
            Text("☰")
        }
        Text(username,
            modifier= Modifier.weight(1f)
        )
        TextButton(
            onClick=
                onLogout
        ){
            Text("Salir")
        }
    }
}