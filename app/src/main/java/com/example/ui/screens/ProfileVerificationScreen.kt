package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeOutline
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.theme.ThemeSuccessLight
import com.example.ui.viewmodel.RoomFinderViewModel

import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileVerificationScreen(
    viewModel: RoomFinderViewModel,
    onNavigateToSavedRooms: () -> Unit,
    modifier: Modifier = Modifier
)  {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val savedListings by viewModel.savedListings.collectAsStateWithLifecycle()
    var showEditProfileDialog by remember  { mutableStateOf(false) }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Text(
                        text = "User Trust & Verification",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    )  { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("profile_screen")
        )  {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = ThemePrimaryDark),
                shape = RoundedCornerShape(16.dp)
            )  {
                Column(modifier = Modifier.padding(18.dp))  {
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(ThemeSecondary),
                            contentAlignment = Alignment.Center
                        )  {
                            Text(
                                text = profile.name.take(1),
                                color = ThemeTextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f))  {
                            Text(
                                text = profile.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = profile.email,
                                fontSize = 12.sp,
                                color = ThemePrimaryLight
                            )
                            Text(
                                text = profile.university,
                                fontSize = 11.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                        IconButton(onClick =  { showEditProfileDialog = true })  {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mode Switcher Pill
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.35f)),
                        shape = RoundedCornerShape(10.dp)
                    )  {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Row(verticalAlignment = Alignment.CenterVertically)  {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column  {
                                    Text(
                                        text = if (profile.isLandlordMode) "Landlord Pro Mode" else "Student Renter Mode",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (profile.isLandlordMode) "Managing listings & maintenance" else "Searching rooms & roommate matches",
                                        color = ThemePrimaryLight,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Switch(
                                checked = profile.isLandlordMode,
                                onCheckedChange =  { viewModel.toggleLandlordMode() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = ThemeSecondary,
                                    checkedTrackColor = ThemePrimary
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Background Check & Verification Badges Center (CRITICAL FEATURE)
            Text(
                text = "Integrated Background Checks & Credentials",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Verified credentials protect both renters and landlords in urban student housing.",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )

            VerificationStatusItem(
                icon = Icons.Default.Badge,
                title = "Government ID Verification",
                subtitle = "State Driver's License & Biometrics verified",
                isVerified = profile.isIdVerified
            )

            Spacer(modifier = Modifier.height(8.dp))

            VerificationStatusItem(
                icon = Icons.Default.School,
                title = "Student Status Verification",
                subtitle = "Enrolled at ${profile.university} (.edu active)",
                isVerified = profile.isStudentVerified
            )

            Spacer(modifier = Modifier.height(8.dp))

            VerificationStatusItem(
                icon = Icons.Default.Security,
                title = "Background Check Record",
                subtitle = "Criminal record & national eviction search passed",
                isVerified = profile.isBackgroundChecked
            )

            Spacer(modifier = Modifier.height(8.dp))

            VerificationStatusItem(
                icon = Icons.Default.CreditScore,
                title = "Credit Score Tier ({profile.creditTier})",
                subtitle = "Verified via TransUnion SmartMove API",
                isVerified = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Saved Rooms Quick Tile
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable  { onNavigateToSavedRooms() },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder()
            )  {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ThemeError.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = ThemeError, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column  {
                            Text("Saved Rooms & Bookmarks", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("${savedListings.size} listings bookmarked", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Text("View →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ThemePrimary)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick =  { viewModel.logout() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(12.dp)
            )  {
                Text("Log Out", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showEditProfileDialog)  {
        var name by remember  { mutableStateOf(profile.name) }
        var email by remember  { mutableStateOf(profile.email) }
        var university by remember  { mutableStateOf(profile.university) }
        
        Dialog(onDismissRequest =  { showEditProfileDialog = false })  {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            )  {
                Column(modifier = Modifier.padding(16.dp))  {
                    Text("Edit Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange =  { name = it },
                        label =  { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange =  { email = it },
                        label =  { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = university,
                        onValueChange =  { university = it },
                        label =  { Text("University / Company") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    )  {
                        OutlinedButton(onClick =  { showEditProfileDialog = false })  {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick =  {
                                viewModel.updateUserProfile(name, email, university, profile.creditTier)
                                showEditProfileDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                        )  {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerificationStatusItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    isVerified: Boolean
)  {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder()
    )  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )  {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically)  {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isVerified) ThemeSuccessLight else ThemeOutline),
                    contentAlignment = Alignment.Center
                )  {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isVerified) ThemeSuccess else ThemeTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column  {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isVerified) ThemeSuccessLight else ThemeOutline
            )  {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (isVerified) ThemeSuccess else ThemeTextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = if (isVerified) "Verified" else "Pending",
                        color = if (isVerified) ThemeSuccess else ThemeTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
