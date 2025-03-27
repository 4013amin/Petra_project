package com.example.shop_app_project.Home_page.Main.ChatScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.data.utils.UtilsRetrofit
import kotlinx.coroutines.launch
import org.json.JSONObject

data class MessageModel(
    val text: String,
    val isSent: Boolean,
    val replyTo: MessageModel? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, phone: String, receiver: String) {
    val messages = remember { mutableStateListOf<MessageModel>() }
    var inputMessage by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<MessageModel?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val chatUrl = "ws://192.168.1.110:2020/ws/chat/$phone/$receiver/"

    val webSocketListener = ChatWebSocketListener { message ->
        println("Received message: $message")
        val json = JSONObject(message)
        val text = json.getString("message")
        val sender = json.getString("sender")
        val replyToJson = json.optJSONObject("reply_to")
        val replyTo = replyToJson?.let {
            val replyText = it.getString("message")
            val replySender = it.getString("sender")
            messages.find { m -> m.text == replyText && m.isSent != (replySender == phone) }
                ?: MessageModel(replyText, replySender == phone)
        }
        messages.add(MessageModel(text, sender == phone, replyTo))
        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
        }
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
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1976D2)
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            state = listState,
            reverseLayout = true
        ) {
            items(messages.reversed()) { message ->
                ChatBubble(
                    message = message,
                    onReplyClick = { replyingTo = it }
                )
            }
        }

        // نمایش پیام در حال ریپلای
        replyingTo?.let { replyMessage ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xFFF5F5F5)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Replying to: ${replyMessage.text}",
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(onClick = { replyingTo = null }) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel reply")
                }
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
                            replyingTo?.let { put("reply_to", it.text) }
                        }.toString()
                        println("Sending message: $jsonMessage")
                        webSocketClient.sendMessage(jsonMessage)
                        messages.add(MessageModel(inputMessage, true, replyingTo))
                        coroutineScope.launch {
                            listState.animateScrollToItem(messages.size - 1)
                        }
                        inputMessage = ""
                        replyingTo = null
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageModel, onReplyClick: (MessageModel) -> Unit) {
    val alignment = if (message.isSent) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isSent) Color(0xFF6200EE) else Color(0xFFE0E0E0)
    val textColor = if (message.isSent) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !message.isSent) { onReplyClick(message) },
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            message.replyTo?.let { replyTo ->
                Text(
                    text = "↳ ${replyTo.text}",
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
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
    if (phone == null) return
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

@Preview(showBackground = true)
@Composable
private fun ShowChatScreen() {
    val navController = rememberNavController()
    ChatScreen(navController = navController, phone = "09362629118", receiver = "09964977267")
}