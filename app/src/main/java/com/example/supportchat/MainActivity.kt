package com.example.supportchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.supportchat.ui.theme.SupportChatTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setContent {
            SupportChatTheme {
                val currentUser = SessionManager.getUser(this)

                if (currentUser == null) {

                    LoginPage(
                        context = this,
                        onLoginSuccess = {
                            recreate()
                        }
                    )
                }

                else{
                    chatViewModel.initializeUser(currentUser)
                    ChatPage(modifier = Modifier, chatViewModel)
                }
            }
        }
    }
}