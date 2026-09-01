package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ListingEntity
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeOutline
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess

@Composable
fun RoomCard(
    listing: ListingEntity,
    onListingClick: () -> Unit,
    onSaveToggle: () -> Unit,
    onVirtualTourClick: () -> Unit,
    modifier: Modifier = Modifier
)  {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .clickable  { onListingClick() }
            .testTag("room_card_{listing.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    )  {
        Column(modifier = Modifier.fillMaxWidth())  {
            // Visual Room Header with Modern Gradient Banner & Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                ThemePrimaryDark,
                                ThemePrimary,
                                ThemeTextPrimary
                            )
                        )
                    )
                    .padding(12.dp)
            )  {
                // Room Type icon badge & status
                Column(
                    modifier = Modifier.align(Alignment.TopStart),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    AvailabilityBadge(status = listing.availabilityStatus)
                    PriceTierPill(
                        basePrice = listing.basePrice,
                        utilitiesPrice = listing.utilitiesPrice,
                        studentDiscountPercent = listing.studentDiscountPercent
                    )
                }

                // Save button top right
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.45f),
                    modifier = Modifier.align(Alignment.TopEnd)
                )  {
                    IconButton(
                        onClick = onSaveToggle,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("save_btn_{listing.id}")
                    )  {
                        Icon(
                            imageVector = if (listing.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save Listing",
                            tint = if (listing.isSaved) ThemeError else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Room Type Banner bottom left
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = listing.roomType,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${listing.squareFeet} sq ft",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp
                    )
                }

                // 360 Virtual Tour Quick Chip bottom right
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = ThemeSecondary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clickable  { onVirtualTourClick() }
                )  {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "360 Tour Available",
                            tint = ThemeTextPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "360° Tour",
                            color = ThemeTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Body Content
            Column(modifier = Modifier.padding(14.dp))  {
                // Price and Title Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                )  {
                    Column(modifier = Modifier.weight(1f))  {
                        Text(
                            text = listing.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(horizontalAlignment = Alignment.End)  {
                        Row(verticalAlignment = Alignment.Bottom)  {
                            Text(
                                text = "₹${listing.basePrice}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ThemePrimary
                            )
                            Text(
                                text = "/mo",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Text(
                            text = if (listing.utilitiesPrice == 0) "Utils Included" else "+₹${listing.utilitiesPrice} util",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Location & University Proximity Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "University",
                        tint = ThemePrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${listing.walkMinutesToCampus}m walk to ${listing.universityNearby}",
                        fontSize = 12.sp,
                        color = ThemePrimaryDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = "Transit",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = listing.metroDistance,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Feature tags (Furnished, Bath, WalkScore)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    if (listing.furnished)  {
                        FeatureMiniTag(label = "Furnished")
                    }
                    if (listing.hasPrivateBath)  {
                        FeatureMiniTag(label = "Private Bath")
                    } else  {
                        FeatureMiniTag(label = "Shared Bath")
                    }
                    FeatureMiniTag(label = "${listing.walkScore} WalkScore")
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Landlord Verification & Rating Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    VerificationBadgesRow(
                        isLandlordVerified = listing.landlordVerified,
                        isBackgroundChecked = listing.backgroundChecked
                    )

                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = ThemeSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${listing.landlordRating}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " ({listing.landlordReviewCount})",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action CTA Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    OutlinedButton(
                        onClick = onVirtualTourClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("tour_btn_{listing.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ThemePrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )  {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("360° Tour", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onListingClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .testTag("view_details_btn_{listing.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ThemePrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )  {
                        Text("View Room", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureMiniTag(label: String)  {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    )  {
        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
