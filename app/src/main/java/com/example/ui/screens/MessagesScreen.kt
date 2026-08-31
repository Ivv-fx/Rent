package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.MessageEntity
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeSurfaceVariant
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: RoomFinderViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
)  {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    var inputMessage by remember  { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickQuestions = listOf(
        "Is this available for Fall Semester?",
        "Can we schedule a 360 video walkthrough?",
        "Are all utilities included in the rent?",
        "What is the security deposit policy?"
    )

    LaunchedEffect(messages.size)  {
        if (messages.isNotEmpty())  {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ThemePrimary),
                            contentAlignment = Alignment.Center
                        )  {
                            Text("M", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column  {
                            Row(verticalAlignment = Alignment.CenterVertically)  {
                                Text(
                                    text = "Rajesh Negi",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Verified Landlord",
                                    tint = ThemePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Verified Landlord Pro • Sunlit Studio Loft",
                                fontSize = 11.sp,
                                color = ThemeSuccess,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    )  { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("messages_screen")
        )  {
            // Safety & Verification Trust Banner
            Surface(
                color = ThemePrimaryLight.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            )  {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ThemeSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Encrypted messaging with verified landlord. Background check confirmed.",
                        fontSize = 11.sp,
                        color = ThemePrimaryDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Message History List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            )  {
                items(messages, key =  { it.id })  { msg ->
                    ChatBubble(message = msg)
                }
            }

            // Quick Inquiry Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            )  {
                items(quickQuestions)  { question ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable  {
                            viewModel.sendMessage(
                                listingId = 1,
                                listingTitle = "Sunlit Studio Loft near HNBGU",
                                text = question
                            )
                        }
                    )  {
                        Text(
                            text = question,
                            fontSize = 11.sp,
                            color = ThemePrimaryDark,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Message Input Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            )  {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    OutlinedTextField(
                        value = inputMessage,
                        onValueChange =  { inputMessage = it },
                        placeholder =  { Text("Write a message to landlord...") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedBorderColor = ThemePrimary,
                            unfocusedBorderColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick =  {
                            if (inputMessage.isNotBlank())  {
                                viewModel.sendMessage(
                                    listingId = 1,
                                    listingTitle = "Sunlit Studio Loft near HNBGU",
                                    text = inputMessage.trim()
                                )
                                inputMessage = ""
                            }
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ThemePrimary)
                            .testTag("send_msg_btn")
                    )  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageEntity)  {
    val isMe = !message.isLandlord

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
    )  {
        // Tour booking badge if present
        if (message.tourBookingDate != null)  {
            Card(
                colors = CardDefaults.cardColors(containerColor = ThemeSecondary.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .width(260.dp)
            )  {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically)  {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = ThemeSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "📅 ${message.tourBookingDate}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemeTextPrimary
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            color = if (isMe) ThemePrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth(0.85f)
        )  {
            Column(modifier = Modifier.padding(12.dp))  {
                Text(
                    text = message.text,
                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
        Text(
            text = if (isMe) "Sent" else "Rajesh Negi • Verified Host",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
        )
    }
}
