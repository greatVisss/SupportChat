package com.example.supportchat

import com.google.firebase.firestore.FirebaseFirestore

object UserManager {
    private val db =
        FirebaseFirestore.getInstance()

    fun loginUser(
        username:String,
        password:String,
        onSuccess:()->Unit,
        onError:(String)->Unit
    ){
        val user =
            username.uppercase()
        db.collection("usuarios")
            .document(user)
            .get()
            .addOnSuccessListener {
                if(!it.exists()){
                    onError("Usuario o contraseña incorrectos")
                    return@addOnSuccessListener
                }

                val savedPass = it.getString("password")

                if(savedPass==password){
                    onSuccess()
                }

                else{
                    onError("Usuario o contraseña incorrectos")
                }
            }

            .addOnFailureListener {
                onError(it.message ?: "Error")
            }
    }

    fun createUser(
        username:String,
        password:String,
        onSuccess:()->Unit,
        onError:(String)->Unit
    ){
        val user = username.uppercase()

        val doc = db.collection("usuarios").document(user)

        doc.get()
            .addOnSuccessListener {
                if(it.exists()){
                    onError("Usuario existente")

                    return@addOnSuccessListener
                }

                val data = hashMapOf(
                        "username" to user,
                        "password" to password
                    )

                doc.set(data)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError(it.message?: "Error")
                    }
            }
    }
}