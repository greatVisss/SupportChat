package com.example.supportchat

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginPage(
    context: Context,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registerMode by remember { mutableStateOf(false) }

    if (registerMode) {
        RegisterPage(
            context = context,
            onBack = {
                registerMode = false
            },
            onRegisterSuccess = {
                onLoginSuccess()
            }
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text("Inicio sesión")

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it.uppercase()
                        .filter {
                            it.isLetterOrDigit()
                        }
                },

                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it.replace(
                        " ", ""
                    )
                },

                singleLine = true,

                visualTransformation = PasswordVisualTransformation(),

                modifier = Modifier.fillMaxWidth()

            )

            Spacer(Modifier.height(20.dp))

            Row() {
                Button(
                    onClick = {
                        UserManager.loginUser(
                            username,
                            password,
                            onSuccess = {
                                SessionManager.saveUser(
                                    context,
                                    username
                                )
                                onLoginSuccess()
                            },
                            onError = {
                                Toast.makeText(
                                    context,
                                    it,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                ) {
                    Text("Entrar")
                }

                TextButton(
                    onClick = { registerMode = true }
                ) {
                    Text("Crear usuario")
                }
            }
        }
    }
}