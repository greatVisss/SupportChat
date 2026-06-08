package com.example.supportchat

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterPage(
    context:Context,
    onBack:()->Unit,
    onRegisterSuccess:()->Unit
){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    LazyColumn(modifier= Modifier.fillMaxSize().background(color = Color(0xfffffee4)),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Text("Support Chat", modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(0xFF446176),
                style = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(10.dp))

            Text("Crear Usuario", modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(0xFFbec0c0),
                style = TextStyle(fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold))
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3eaaae),
                    cursorColor = Color(0xFF3eaaae),
                    focusedTextColor = Color(0xFF446176),
                    unfocusedTextColor = Color(0xFF446176)
                ),
                placeholder = {Text("USUARIO",
                    color = Color(0xFFbec0c0),
                    style = TextStyle(fontStyle = FontStyle.Italic,
                        fontSize = 15.sp))},
                shape = RoundedCornerShape(15.dp),
                value=username,
                onValueChange={ username= it.uppercase()
                            .filter {
                                    c->
                                c.isLetterOrDigit()
                            }
                },

                enabled=!loading,

                singleLine=true,

                modifier= Modifier.fillMaxWidth().padding(horizontal = 50.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3eaaae),
                    cursorColor = Color(0xFF3eaaae),
                    focusedTextColor = Color(0xFF446176),
                    unfocusedTextColor = Color(0xFF446176)
                ),
                placeholder = {Text("Contraseña",
                    color = Color(0xFFbec0c0),
                    style = TextStyle(fontStyle = FontStyle.Italic,
                        fontSize = 15.sp))},
                shape = RoundedCornerShape(15.dp),
                value=password,
                onValueChange={
                    password=
                        it.replace(
                            " ",
                            ""
                        )
                },

                enabled=!loading,

                singleLine=true,

                visualTransformation= PasswordVisualTransformation(),
                modifier= Modifier.fillMaxWidth().padding(horizontal = 50.dp)
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3eaaae),
                    cursorColor = Color(0xFF3eaaae),
                    focusedTextColor = Color(0xFF446176),
                    unfocusedTextColor = Color(0xFF446176)
                ),
                placeholder = {Text("Confirmar Contraseña",
                    color = Color(0xFFbec0c0),
                    style = TextStyle(fontStyle = FontStyle.Italic,
                        fontSize = 15.sp))},
                shape = RoundedCornerShape(15.dp),
                value=confirm,
                onValueChange={
                    confirm=
                        it.replace(
                            " ",
                            ""
                        )
                },

                enabled=!loading,

                singleLine=true,

                visualTransformation= PasswordVisualTransformation(),

                modifier= Modifier.fillMaxWidth().padding(horizontal = 50.dp)

            )

            Spacer(Modifier.height(20.dp))

            Row{
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF446176)),
                    enabled=!loading,
                    onClick={
                        if(username.isBlank() || password.isBlank() || confirm.isBlank()
                        ){
                            Toast.makeText(
                                context,
                                "Llena todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        if(password!=confirm){
                            Toast.makeText(
                                context,
                                "Contraseñas distintas",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        loading=true

                        UserManager.createUser(username, password,
                            onSuccess={
                                loading=false
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

                            onError={
                                loading=false
                                Toast.makeText(
                                    context,
                                    it,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                ){

                    Text(
                        if(loading)
                            "..."
                        else
                            "Crear", color = Color(0xfffffee4)
                    )
                }

                TextButton(
                    enabled=!loading,
                    onClick=onBack
                ){
                    Text("Volver", color = Color(0xFF446176))
                }
            }
        }
    }
}