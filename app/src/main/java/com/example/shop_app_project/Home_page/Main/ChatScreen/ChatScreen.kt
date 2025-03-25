package com.example.shop_app_project.Home_page.Main.ChatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.shop_app_project.data.utils.UtilsRetrofit
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, phone: String, receiver: String) {
    val messages = remember { mutableStateListOf<MessageModel>() }
    var inputMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val chatUrl = "ws://192.168.1.110:2020/ws/chat/$phone/$receiver/"

    val webSocketListener = ChatWebSocketListener { message ->
        println("Received message: $message") // دیباگ
        val json = JSONObject(message)
        val text = json.getString("message")
        val sender = json.getString("sender")
        messages.add(MessageModel(text, sender == phone))
    }
    val webSocketClient = remember { WebSocketClient(chatUrl, webSocketListener) }

    LaunchedEffect(Unit) {
        webSocketClient.connect()
    }

    DisposableEffect(Unit) {
        onDispose { webSocketClient.close() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Chat with $receiver") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                singleLine = true
            )
            IconButton(
                onClick = {
                    if (inputMessage.isNotBlank()) {
                        val jsonMessage = JSONObject().apply {
                            put("sender", phone)
                            put("receiver", receiver)
                            put("message", inputMessage)
                        }.toString()
                        println("Sending message: $jsonMessage")
                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(MessageModel(inputMessage, true))
                        inputMessage = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUsersScreen(navController: NavController, phone: String) {
    val chatUsers = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(phone) {
        coroutineScope.launch {
            try {
                val response = UtilsRetrofit.api.getChatUsers(phone)
                if (response.isSuccessful) {
                    response.body()?.let { chatUsersResponse ->
                        chatUsers.clear()
                        // فقط مقادیر غیر null را اضافه می‌کنیم
                        chatUsers.addAll(chatUsersResponse.users.mapNotNull { it.phone })
                    }
                } else {
                    error = "Failed to load users"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Messages From Users") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            error != null -> {
                Text(
                    text = error ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            chatUsers.isEmpty() -> {
                Text(
                    text = "No messages yet",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(chatUsers) { senderPhone ->
                        UserItem(
                            phone = senderPhone,
                            onClick = {
                                navController.navigate("chat/$phone/$senderPhone")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(phone: String?, onClick: () -> Unit) {
    if (phone == null) return // اگر phone null باشد، چیزی نمایش داده نمی‌شود
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = phone,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}