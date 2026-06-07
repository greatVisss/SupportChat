package com.example.supportchat

import android.R.attr.text
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel: ViewModel() {

    private var chatCreated = false
    private var initialized = false
    var currentUser=""
    var currentChatId=""
    val chatList by lazy {
        mutableStateListOf<ChatModel>()
    }

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }
    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-3.5-flash",
        apiKey = Constants.apiKey
    )

    private val emotionalPrompt = """Eres un asistente de apoyo emocional profesional.  
                    Tu propósito es acompañar a la persona en su proceso de reflexión y bienestar dando retroalimentacion motivacional.  
                    
                    Objetivos principales:  
                    - Escuchar activamente y responder de manera breve pero significativa.  
                    - Mostrar empatía genuina en un tono cálido y conversacional.  
                    - Ayudar a la persona a reflexionar sobre lo que siente o piensa.  
                    - Ofrecer motivación realista y práctica, evitando frases vacías.  
                    - Evitar juicios o críticas; mantén neutralidad y respeto.  
                    - Nunca afirmar que eres humano; mantén tu rol como asistente.  
                    - Recomendar apoyo profesional cuando detectes riesgo o señales de alarma.  
                    
                    Estilo de respuesta:  
                    - Sé conciso.  
                    - Usa lenguaje claro y accesible.  
                    - Mantén un tono cercano, empático y motivador.  
                    - Evita tecnicismos innecesarios.  
                    - Refuerza la idea de que la persona no está sola y puede buscar ayuda externa. 
                     """

    fun sendMessage(question : String){
        viewModelScope.launch {
            try {
                if(!chatCreated){
                    chatCreated = true
                    val generatedTitle = generateChatTitle(question)
                    ChatManager.createChat(
                        currentUser,
                        currentChatId,
                        generatedTitle
                    )
                    loadChats()
                }

                val historyContent = mutableListOf(
                    content(
                        role = "user"
                    ){
                        text(
                            emotionalPrompt
                        )
                    }
                )

                historyContent.addAll(
                    messageList.map {
                        content(
                            it.role
                        ){
                            text(
                                it.message
                            )
                        }
                    }
                )

                val chat = generativeModel.startChat(
                    history = historyContent
                )

                //Crea mensaje saveable
                val userMsg= MessageModel(
                        question,
                        "user"
                    )

                messageList.add(userMsg)

                ChatManager.saveMessage(
                    currentUser,
                    currentChatId,
                    userMsg
                )
                //////

                messageList.add(MessageModel("...", role = "model"))

                val response = chat.sendMessage(question)

                messageList.removeAt(messageList.lastIndex)

                //Crea respuesta saveable
                val modelMsg= MessageModel(
                        response.text.toString(),
                        "model"
                    )

                messageList.add(modelMsg)

                ChatManager.saveMessage(
                    currentUser,
                    currentChatId,
                    modelMsg
                )
                /////////

            }catch (e: Exception){
                messageList.removeAt(messageList.lastIndex)
                messageList.add(MessageModel("Error: " + e.message.toString(), "model"))
            }
        }
    }

    fun initializeUser(username:String){
        if(initialized)
            return
        initialized = true
        currentUser = username
        createNewChat()
        loadChats()
    }

    fun createNewChat(){
        currentChatId =
            UUID.randomUUID().toString()

        chatCreated = false

        messageList.clear()
    }

    fun loadChats(){
        FirebaseFirestore
            .getInstance()
            .collection("userschats")
            .document(currentUser)
            .collection("chats")
            .get()
            .addOnSuccessListener {
                chatList.clear()
                for(doc in it.documents){
                    chatList.add(
                        ChatModel(
                            id=
                                doc.getString("id")
                                    ?: "",
                            title=
                                doc.getString("title")
                                    ?: "",
                            timestamp=
                                doc.getLong("timestamp")
                                    ?:0
                        )
                    )
                }
            }
    }

    fun loadChat(chatId:String){
        currentChatId = chatId
        chatCreated = true
        messageList.clear()

        FirebaseFirestore
            .getInstance()
            .collection("userschats")
            .document(currentUser)
            .collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy(
                "timestamp"
            )
            .get()
            .addOnSuccessListener {
                for(doc in it.documents){
                    messageList.add(
                        MessageModel(
                            doc.getString(
                                "message"
                            ) ?: "",
                            doc.getString(
                                "role"
                            ) ?: "",
                            doc.getLong(
                                "timestamp"
                            ) ?:0
                        )
                    )
                }
            }
    }

    private suspend fun generateChatTitle(firstMessage:String):String{
        return try{
            val response = generativeModel.generateContent(
                "Genera un título MUY corto (máximo 4 palabras) para esta conversación: $firstMessage"
            )

            response.text
                ?.take(40)
                ?: "Nuevo chat"
        }

        catch(_:Exception){
            "Nuevo chat"
        }
    }
}