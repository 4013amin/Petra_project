package com.example.shop_app_project.Home_page.Main.ChatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, username: String, receiver: String) {
    val messages = remember { mutableStateListOf<MessageModel>() }
    var inputMessage by remember { mutableStateOf("") }

    val chatUrl = "ws://192.168.1.105:2020/ws/chat/$receiver/"

    val webSocketListener = ChatWebSocketListener { message ->
        messages.add(MessageModel(message, false))
    }
    val webSocketClient = remember { WebSocketClient(chatUrl, webSocketListener) }

    LaunchedEffect(Unit) {
        webSocketClient.connect()
    }

    DisposableEffect(Unit) {
        onDispose {
            webSocketClient.close()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "چت", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "بازگشت",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE))
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(message = message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("پیام خود را بنویسید...") },
                singleLine = true,
            )
            IconButton(
                onClick = {
                    if (inputMessage.isNotBlank()) {
                        val jsonMessage = JSONObject().apply {
                            put("sender", username)
                            put("receiver", receiver)
                            put("message", inputMessage)
                        }.toString()

                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(MessageModel(inputMessage, true))
                        inputMessage = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "ارسال",
                    tint = Color(0xFF6200EE)
                )
            }
        }
    }
}

// مدل پیام
data class MessageModel(val text: String, val isSent: Boolean)

// آیتم پیام
@Composable
fun ChatBubble(message: MessageModel) {
    val alignment = if (message.isSent) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isSent) Color(0xFF6200EE) else Color(0xFFE0E0E0)
    val textColor = if (message.isSent) Color.White else Color.Black

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
        ) {
            Text(text = message.text, color = textColor)
        }
    }
}