package com.example.roomiesplit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomiesplit.data.model.ChatMessage
import com.example.roomiesplit.data.model.Flat
import com.example.roomiesplit.data.model.Member
import com.example.roomiesplit.ui.components.AvatarBadge
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MustardContainer
import com.example.ui.theme.OnMustardContainer
import com.example.ui.theme.TealContainer
import com.example.ui.theme.WarmIvory
import com.example.ui.theme.WarmMustard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    flat: Flat,
    activeMember: Member,
    messages: List<ChatMessage>,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickReactions = listOf("Paid 👍", "Chai ready ☕", "Maggi time 🍜", "Ludo match? 🎲", "Who has the keys? 🔑", "Going for groceries 🛒")

    // Scroll to bottom on new message
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        // Chat Header Banner
        Surface(
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Forum,
                        contentDescription = "Chat",
                        tint = DeepTeal,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${flat.name} Group Chat",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Shared transparently among all flatmates",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isMe = msg.senderId == activeMember.id

                if (msg.isSystemAnnouncement) {
                    // System Notification Bubble
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MustardContainer.copy(alpha = 0.7f))
                                .border(1.dp, Color(0xFFE2D6C0), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = msg.text,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnMustardContainer,
                                lineHeight = 16.sp
                            )
                        }
                    }
                } else {
                    // Roommate chat message
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        if (!isMe) {
                            AvatarBadge(initial = msg.avatarInitial, size = 28.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Column(
                            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            if (!isMe) {
                                Text(
                                    text = msg.senderName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 14.dp,
                                            topEnd = 14.dp,
                                            bottomStart = if (isMe) 14.dp else 2.dp,
                                            bottomEnd = if (isMe) 2.dp else 14.dp
                                        )
                                    )
                                    .background(if (isMe) DeepTeal else Color.White)
                                    .border(
                                        1.dp,
                                        if (isMe) DeepTeal else Color(0xFFE2DDD3),
                                        RoundedCornerShape(
                                            topStart = 14.dp,
                                            topEnd = 14.dp,
                                            bottomStart = if (isMe) 14.dp else 2.dp,
                                            bottomEnd = if (isMe) 2.dp else 14.dp
                                        )
                                    )
                                    .padding(horizontal = 12.dp, vertical = 9.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    fontSize = 14.sp,
                                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onBackground
                                )
                            }

                            Text(
                                text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(msg.timestamp)),
                                fontSize = 10.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Quick reactions chips row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            quickReactions.forEach { qr ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFDDD5C7), RoundedCornerShape(16.dp))
                        .clickable { onSendMessage(qr) }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = qr, fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        // Bottom Message Input Bar
        Surface(
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Message flatmates...") },
                    shape = RoundedCornerShape(20.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput.trim())
                                textInput = ""
                            }
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            onSendMessage(textInput.trim())
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(DeepTeal)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
