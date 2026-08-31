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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.CommunityEventEntity
import com.example.data.models.ForumPostEntity
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityForumScreen(
    viewModel: RoomFinderViewModel,
    modifier: Modifier = Modifier
)  {
    val forumPosts by viewModel.forumPosts.collectAsStateWithLifecycle()
    val events by viewModel.communityEvents.collectAsStateWithLifecycle()

    var selectedTabIndex by remember  { mutableIntStateOf(0) }
    var selectedCategoryFilter by remember  { mutableStateOf("All Topics") }
    var showCreatePostDialog by remember  { mutableStateOf(false) }

    val categoryFilters = listOf("All Topics", "Roommate Search", "Subleases", "Campus Tips", "Furniture Swap")

    val filteredPosts = forumPosts.filter  {
        selectedCategoryFilter == "All Topics" || it.category.equals(selectedCategoryFilter, ignoreCase = true)
    }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Column  {
                        Text(
                            text = "Tenant Community & Hub",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Local forums, roommates & student events",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton =  {
            if (selectedTabIndex == 0)  {
                ExtendedFloatingActionButton(
                    onClick =  { showCreatePostDialog = true },
                    containerColor = ThemePrimary,
                    contentColor = Color.White,
                    icon =  { Icon(Icons.Default.Add, contentDescription = null) },
                    text =  { Text("New Post", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("new_post_fab")
                )
            }
        }
    )  { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("community_forum_screen")
        )  {
            // Tab Selection (Forums vs Events)
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ThemePrimary
            )  {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick =  { selectedTabIndex = 0 },
                    text =  {
                        Row(verticalAlignment = Alignment.CenterVertically)  {
                            Icon(Icons.Default.Forum, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Tenant Forums", fontSize = 12.sp, fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick =  { selectedTabIndex = 1 },
                    text =  {
                        Row(verticalAlignment = Alignment.CenterVertically)  {
                            Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Community Events ({events.size})", fontSize = 12.sp, fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                )
            }

            if (selectedTabIndex == 0)  {
                // Topic Filters
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    items(categoryFilters)  { cat ->
                        val isSelected = selectedCategoryFilter == cat
                        FilterChip(
                            selected = isSelected,
                            onClick =  { selectedCategoryFilter = cat },
                            label =  { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Forum Posts List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                )  {
                    items(filteredPosts, key =  { it.id })  { post ->
                        ForumPostCard(
                            post = post,
                            onLikeToggle =  {
                                viewModel.toggleLikePost(post.id, post.isLiked, post.likesCount)
                            }
                        )
                    }
                    item  { Spacer(modifier = Modifier.height(80.dp)) }
                }
            } else  {
                // Community Events Tab
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                )  {
                    items(events, key =  { it.id })  { event ->
                        CommunityEventCard(
                            event = event,
                            onRsvpToggle =  {
                                viewModel.toggleEventRsvp(event.id, event.isRsvpd, event.rsvpCount)
                            }
                        )
                    }
                    item  { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }

        if (showCreatePostDialog)  {
            CreateForumPostDialog(
                onDismiss =  { showCreatePostDialog = false },
                onSubmit =  { title, cat, content ->
                    viewModel.createForumPost(title, cat, content)
                }
            )
        }
    }
}

@Composable
fun ForumPostCard(
    post: ForumPostEntity,
    onLikeToggle: () -> Unit
)  {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    )  {
        Column(modifier = Modifier.padding(14.dp))  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ThemePrimary),
                        contentAlignment = Alignment.Center
                    )  {
                        Text(post.authorName.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column  {
                        Text(post.authorName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("${post.university} • ${post.timestamp}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )  {
                    Text(
                        text = post.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ThemePrimaryDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = post.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = post.content,
                fontSize = 12.sp,
                color = ThemeTextSecondary,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Row(
                    modifier = Modifier.clickable  { onLikeToggle() },
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) ThemeError else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likesCount} Helpful",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (post.isLiked) ThemeError else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "Replies",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.commentsCount} replies",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityEventCard(
    event: CommunityEventEntity,
    onRsvpToggle: () -> Unit
)  {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder()
    )  {
        Column(modifier = Modifier.padding(16.dp))  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ThemePrimaryLight
                )  {
                    Text(
                        text = event.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemePrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = ThemeSuccess, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${event.rsvpCount} Attending", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ThemeSuccess)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = event.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically)  {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = ThemePrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${event.date} • ${event.time}", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically)  {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = ThemeError, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = event.location, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onRsvpToggle,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (event.isRsvpd) ThemeSuccess else ThemePrimary
                ),
                shape = RoundedCornerShape(8.dp)
            )  {
                Text(
                    text = if (event.isRsvpd) "✓ RSVP'd (See you there!)" else "RSVP to Event",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Dialog: Create Forum Post
@Composable
private fun CreateForumPostDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, category: String, content: String) -> Unit
)  {
    var title by remember  { mutableStateOf("") }
    var content by remember  { mutableStateOf("") }
    var category by remember  { mutableStateOf("Roommate Search") }

    Dialog(onDismissRequest = onDismiss)  {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )  {
            Column(modifier = Modifier.padding(18.dp))  {
                Text(
                    text = "Post in Tenant Community Hub",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange =  { title = it },
                    label =  { Text("Topic Title") },
                    placeholder =  { Text("e.g. Looking for HNBGU roommate for Spring") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    items(listOf("Roommate Search", "Subleases", "Campus Tips", "Furniture Swap"))  { cat ->
                        val isSelected = category == cat
                        FilterChip(
                            selected = isSelected,
                            onClick =  { category = cat },
                            label =  { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange =  { content = it },
                    label =  { Text("Post Details / Contact Info") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f))  {
                        Text("Cancel")
                    }
                    Button(
                        onClick =  {
                            if (title.isNotBlank())  {
                                onSubmit(title, category, content)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                    )  {
                        Text("Publish", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
