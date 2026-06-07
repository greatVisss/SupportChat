package com.example.supportchat

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
fun RegisterPage(
    context:Context,
    onBack:()->Unit,
    onRegisterSuccess:()->Unit
){
    var username by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var confirm by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        item() {
            Text("Crear usuario")

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it.uppercase().filter { it.isLetterOrDigit() }
                },

                singleLine = true,

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password =
                        it.replace(" ", "")
                },

                singleLine = true,

                visualTransformation = PasswordVisualTransformation(),

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = confirm,
                onValueChange = {
                    confirm = it.replace(" ", "")
                },

                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            Row() {
                Button(
                    onClick = {
                        if (password != confirm) {
                            Toast.makeText(
                                context,
                                "Contraseñas distintas",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        UserManager.createUser(
                            username,
                            password,
                            onSuccess = {
                                SessionManager.saveUser(
                                    context,
                                    username
                                )
                                Toast.makeText(
                                    context,
                                    "Usuario creado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onRegisterSuccess()
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
                    Text("Crear")
                }
                TextButton(onClick = onBack) {
                    Text("Volver")
                }
            }
        }
    }
}