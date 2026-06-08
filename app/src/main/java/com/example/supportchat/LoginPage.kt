package com.example.supportchat

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoginPage(
    context: Context,
    onLoginSuccess: () -> Unit
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registerMode by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    if(registerMode){
        RegisterPage(
            context = context,
            onBack = {
                registerMode=false
            },
            onRegisterSuccess = {
                onLoginSuccess()
            }
        )
        return
    }

    LazyColumn(modifier = Modifier
                .fillMaxSize()
        .background(color = Color(0xfffffee4)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Text("Support Chat", modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(0xFF446176),
                style = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.Bold))

            Image(modifier= Modifier.size(100.dp),
                painter= painterResource(R.drawable.suppchat_icon),
                contentDescription=null
            )


            Spacer(Modifier.height(10.dp))

            Text("Iniciar Sesión", modifier = Modifier.fillMaxWidth(),
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
                onValueChange={
                    username= it.uppercase()
                            .filter {
                                    c->
                                c.isLetterOrDigit()
                            }
                },
                singleLine=true,
                enabled=!loading,
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
                    password= it.replace(
                            " ",
                            ""
                        )
                },

                visualTransformation= PasswordVisualTransformation(),

                singleLine=true,

                enabled=!loading,

                modifier= Modifier.fillMaxWidth().padding(horizontal = 50.dp)
            )

            Spacer(Modifier.height(20.dp))

            Row{
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF446176)),
                    enabled=!loading,
                    onClick={
                        if(
                            username.isBlank() || password.isBlank()
                        ){
                            Toast.makeText(
                                context,
                                "Llena todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }
                        loading=true
                        UserManager.loginUser(username, password,
                            onSuccess={ loading=false
                                SessionManager.saveUser(context, username)
                                onLoginSuccess()
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
                            "Entrar", color = Color(0xfffffee4)
                    )
                }

                TextButton(
                    enabled=!loading,
                    onClick={
                        registerMode=true
                    }
                ){
                    Text("Crear usuario", color = Color(0xFF446176))
                }
            }
        }
    }
}