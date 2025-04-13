package com.example.shop_app_project.Home_page.Main.ChatScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.shop_app_project.R
import com.example.shop_app_project.data.utils.UtilsRetrofit
import kotlinx.coroutines.launch
import org.json.JSONObject

data class MessageModel(
    val id: Int? = null,
    val text: String,
    val isSent: Boolean,
    val replyTo: MessageModel? = null,
    val status: MessageStatus = MessageStatus.SENT,
    val timestamp: String? = null
)

enum class MessageStatus { SENT, DELIVERED, SEEN }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, phone: String, receiver: String) {
    val messages = remember { mutableStateListOf<MessageModel>() }
    var inputMessage by remember { mutableStateOf("") }
    var replyingTo by remember { mutableStateOf<MessageModel?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    val chatUrl = "ws://192.168.13.101:2020/ws/chat/$phone/$receiver/"

    LaunchedEffect(Unit) {
        NotificationHelper.createNotificationChannel(context)
    }

    val webSocketListener = ChatWebSocketListener { message ->
        val json = JSONObject(message)
        val id = json.optInt("id", -1).takeIf { it != -1 }
        val text = json.getString("message")
        val sender = json.getString("sender")
        val timestamp = json.getString("timestamp")
        val statusStr = json.optString("status", "SENT").uppercase()
        val status = try {
            MessageStatus.valueOf(statusStr)
        } catch (e: IllegalArgumentException) {
            MessageStatus.SENT
        }
        val replyToJson = json.optJSONObject("reply_to")
        val replyTo = replyToJson?.let {
            val replyId = it.optInt("id", -1).takeIf { it != -1 }
            val replyText = it.getString("message")
            val replySender = it.getString("sender")
            messages.find { m -> m.id == replyId } ?: MessageModel(
                null,
                replyText,
                replySender == phone
            )
        }

        val existingMessageIndex = messages.indexOfFirst { it.id == id }
        if (existingMessageIndex != -1) {
            messages[existingMessageIndex] = messages[existingMessageIndex].copy(status = status)
        } else {
            messages.add(MessageModel(id, text, sender == phone, replyTo, status, timestamp))
            messages.sortBy { it.timestamp }
            if (sender != phone) {
                NotificationHelper.showNotification(
                    context = context,
                    title = "پیام جدید از $sender",
                    message = text,
                    phone = phone,
                    receiver = receiver
                )
            }
        }
        coroutineScope.launch {
            if (messages.isNotEmpty()) {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    val webSocketClient = remember { WebSocketClient(chatUrl, webSocketListener) }

    LaunchedEffect(Unit) {
        webSocketClient.connect()
        coroutineScope.launch {
            messages.filter { !it.isSent && it.status != MessageStatus.SEEN }.forEach { msg ->
                val jsonMessage = JSONObject().apply {
                    put("sender", phone)
                    put("receiver", receiver)
                    put("status", "SEEN")
                }.toString()
                webSocketClient.sendMessage(jsonMessage)
            }
        }
    }

    DisposableEffect(Unit) { onDispose { webSocketClient.close() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
    ) {
        TopAppBar(
            title = { Text("Chat with $receiver", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E88E5))
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            state = listState,
            reverseLayout = false
        ) {
            if (messages.isEmpty()) {
                item {
                    Text(
                        text = "هیچ پیامی وجود ندارد",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = Color.Gray
                    )
                }
            } else {
                items(messages) { message ->
                    ChatBubble(message = message, onReplyClick = { replyingTo = it })
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        replyingTo?.let { replyMessage ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .background(Color(0xFFE3F2FD)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Replying to: ${replyMessage.text}",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = Color(0xFF1E88E5)
                )
                IconButton(onClick = { replyingTo = null }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cancel reply",
                        tint = Color.Gray
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(24.dp)),
                placeholder = { Text("Type a message...", color = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1E88E5),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color(0xFF1E88E5),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputMessage.isNotBlank()) {
                        val jsonMessage = JSONObject().apply {
                            put("sender", phone)
                            put("receiver", receiver)
                            put("message", inputMessage)
                            put("status", "SENT")
                            replyingTo?.let { put("reply_to_id", it.id) }
                        }.toString()
                        webSocketClient.sendMessage(jsonMessage)
                        coroutineScope.launch {
                            if (messages.isNotEmpty()) {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                        inputMessage = ""
                        replyingTo = null
                    }
                },
                modifier = Modifier.background(
                    Color(0xFF1E88E5),
                    RoundedCornerShape(50)
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageModel, onReplyClick: (MessageModel) -> Unit) {
    val alignment = if (message.isSent) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isSent) Color(0xFFB3E5FC) else Color.White
    val textColor = Color.Black

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .clickable(enabled = !message.isSent) { onReplyClick(message) },
        horizontalArrangement = if (message.isSent) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(backgroundColor, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            message.replyTo?.let { replyTo ->
                Text(
                    text = "↳ ${replyTo.text}",
                    color = textColor.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message.timestamp?.substring(11, 16) ?: "",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
                if (message.isSent) {
                    Spacer(modifier = Modifier.width(4.dp))
                    when (message.status) {
                        MessageStatus.SENT -> Icon(
                            Icons.Default.Done,
                            "Sent",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )

                        MessageStatus.DELIVERED -> Icon(
                            Icons.Default.Done,
                            "Delivered",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )

                        MessageStatus.SEEN -> Icon(
                            painterResource(id = R.drawable.seen),
                            "Seen",
                            tint = Color(0xFF1E88E5),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
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
                    error = "خطای سیستمی"
                }
            } catch (e: Exception) {
                error = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteChat(senderPhone: String) {
        coroutineScope.launch {
            try {
                val response = UtilsRetrofit.api.deleteChat(phone, senderPhone)
                if (response.isSuccessful) {
                    chatUsers.remove(senderPhone)
                } else {
                    error = "Failed to delete chat"
                }
            } catch (e: Exception) {
                error = "Error deleting chat: ${e.message}"
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
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E88E5))
        )

        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            error != null -> Text(
                text = error ?: "Unknown error",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(15.dp)
            )

            chatUsers.isEmpty() -> Text(
                text = "هنوز پیامی وجود ندارد",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            else -> LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(chatUsers) { senderPhone ->
                    UserItem(
                        phone = senderPhone,
                        onClick = { navController.navigate("chat/$phone/$senderPhone") },
                        onDeleteChat = { deleteChat(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserItem(
    phone: String?,
    onClick: () -> Unit,
    onDeleteChat: (String) -> Unit
) {
    if (phone == null) return

    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { showDialog = true }
            ),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = phone,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("حذف چت") },
            text = { Text("آیا می‌خواهید این چت را حذف کنید؟") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        onDeleteChat(phone)
                        showDialog = false
                    }
                }) {
                    Text("بله")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("خیر")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ShowChatScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Chat Screen") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            UserItem(
                phone = "09362629118",
                onClick = {},
                onDeleteChat = {}
            )
        }
    }
}