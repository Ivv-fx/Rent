package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ListingEntity
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess

@Composable
fun MapPreviewCard(
    listing: ListingEntity,
    modifier: Modifier = Modifier
)  {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .testTag("map_preview_card"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    )  {
        Column(modifier = Modifier.padding(14.dp))  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = ThemeError,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Location & Neighborhood",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ThemeSuccess.copy(alpha = 0.12f)
                )  {
                    Text(
                        text = "${listing.walkScore} WalkScore®",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ThemeSuccess,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = listing.address,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Map graphic canvas representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE2E8F0))
            )  {
                // Procedural stylized street map canvas
                Canvas(modifier = Modifier.fillMaxWidth().height(140.dp))  {
                    val w = size.width
                    val h = size.height

                    // Background map land
                    drawRect(color = Color(0xFFF1F5F9))

                    // Green Park patch
                    drawRect(
                        color = Color(0xFFDCFCE7),
                        topLeft = Offset(w * 0.65f, 10f),
                        size = Size(w * 0.3f, h * 0.45f)
                    )

                    // River / Water body
                    drawRect(
                        color = Color(0xFFE0F2FE),
                        topLeft = Offset(0f, 0f),
                        size = Size(w * 0.12f, h)
                    )

                    // Major Roads
                    val roadColor = Color.White
                    // Main horizontal avenue
                    drawRect(color = roadColor, topLeft = Offset(0f, h * 0.48f), size = Size(w, 18f))
                    drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(0f, h * 0.48f), size = Size(w, 1.5f))
                    drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(0f, h * 0.48f + 18f), size = Size(w, 1.5f))

                    // Secondary horizontal street
                    drawRect(color = roadColor, topLeft = Offset(0f, h * 0.82f), size = Size(w, 12f))

                    // Vertical avenues
                    drawRect(color = roadColor, topLeft = Offset(w * 0.35f, 0f), size = Size(16f, h))
                    drawRect(color = roadColor, topLeft = Offset(w * 0.65f, 0f), size = Size(16f, h))

                    // University Campus zone
                    drawCircle(
                        color = ThemePrimaryLight,
                        radius = 24f,
                        center = Offset(w * 0.72f, h * 0.28f)
                    )
                }

                // Property Pin marker
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )  {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(ThemeError)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    )  {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Listing Pin",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ThemeTextPrimary.copy(alpha = 0.85f),
                        modifier = Modifier.padding(top = 2.dp)
                    )  {
                        Text(
                            text = "₹${listing.basePrice}/mo",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                // University Pin badge
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ThemePrimaryDark.copy(alpha = 0.9f))
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${listing.universityNearby.take(16)}...",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Commute details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )  {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )  {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Icon(
                            imageVector = Icons.Default.DirectionsWalk,
                            contentDescription = null,
                            tint = ThemePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column  {
                            Text(
                                text = "${listing.walkMinutesToCampus} min walk",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "to Main Campus",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )  {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Icon(
                            imageVector = Icons.Default.DirectionsBus,
                            contentDescription = null,
                            tint = ThemeSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column  {
                            Text(
                                text = "Subway/Bus",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = listing.metroDistance.take(18),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Maps launcher button
            Button(
                onClick =  {
                    val mapUri = Uri.parse("geo:0,0?q={Uri.encode(listing.address)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                    try  {
                        context.startActivity(mapIntent)
                    } catch (e: Exception)  {
                        // Fallback browser URL
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/?api=1&query={Uri.encode(listing.address)}")
                        )
                        context.startActivity(webIntent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("open_maps_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary),
                shape = RoundedCornerShape(8.dp)
            )  {
                Icon(
                    imageVector = Icons.Default.Navigation,
                    contentDescription = "Open in Google Maps",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Open Location in Google Maps",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
