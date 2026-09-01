package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.PriceTierPill
import com.example.ui.components.VirtualTourViewer
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimary
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtualTourViewerScreen(
    listingId: Long,
    viewModel: RoomFinderViewModel,
    onBack: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToChat: (Long, String) -> Unit,
    modifier: Modifier = Modifier
)  {
    val listings by viewModel.rawListings.collectAsStateWithLifecycle()
    val listing = listings.find  { it.id == listingId }

    if (listing == null)  {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)  {
            Text("Listing not found")
        }
        return
    }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Column  {
                        Text(
                            text = "360° Virtual Walkthrough",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = listing.title,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon =  {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("tour_back_btn"))  {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .testTag("virtual_tour_screen")
        )  {
            // Interactive 360 Viewer
            VirtualTourViewer(
                listingId = listing.id,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pricing & Availability Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                AvailabilityBadge(status = listing.availabilityStatus)
                PriceTierPill(
                    basePrice = listing.basePrice,
                    utilitiesPrice = listing.utilitiesPrice,
                    studentDiscountPercent = listing.studentDiscountPercent
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Proximity highlight card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            )  {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = ThemePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column  {
                        Text(
                            text = "${listing.walkMinutesToCampus} min walk to ${listing.universityNearby}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${listing.metroDistance} • ${listing.address}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Button(
                onClick =  { onNavigateToChat(listing.id, listing.title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("tour_chat_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary),
                shape = RoundedCornerShape(10.dp)
            )  {
                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chat with Landlord ({listing.landlordName.take(12)}...)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick =  { onNavigateToDetail(listing.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("tour_full_detail_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = ThemePrimaryDark),
                shape = RoundedCornerShape(10.dp)
            )  {
                Text("View Full Room Specs & Pricing Breakdown", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
