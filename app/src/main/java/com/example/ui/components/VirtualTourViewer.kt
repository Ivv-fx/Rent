package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.Deck
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Shower
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.TourHotspot
import com.example.data.models.VirtualTourRoom
import com.example.data.sample.SampleData
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess

@Composable
fun VirtualTourViewer(
    listingId: Long,
    modifier: Modifier = Modifier
)  {
    val rooms = remember(listingId)  { SampleData.getVirtualTourForListing(listingId) }
    var selectedRoomIndex by remember  { mutableStateOf(0) }
    val currentRoom = rooms.getOrElse(selectedRoomIndex)  { rooms.first() }

    var panOffsetPercent by remember  { mutableFloatStateOf(0.5f) } // Horizontal 360 pan 0..1
    var isNightMode by remember  { mutableStateOf(false) }
    var activeHotspot by remember  { mutableStateOf<TourHotspot?>(null) }
    var showSpecsOverlay by remember  { mutableStateOf(false) }

    // Pulsing animation for hotspots
    val infiniteTransition = rememberInfiniteTransition(label = "hotspot_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    )  {
        Column(modifier = Modifier.fillMaxWidth())  {
            // Room Selection Bar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )  {
                items(rooms.indices.toList())  { index ->
                    val room = rooms[index]
                    val isSelected = index == selectedRoomIndex
                    val icon: ImageVector = when (room.roomType)  {
                        "Bedroom" -> Icons.Default.Bed
                        "Bathroom" -> Icons.Default.Shower
                        "Kitchen" -> Icons.Default.Countertops
                        else -> Icons.Default.Deck
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick =  {
                            selectedRoomIndex = index
                            activeHotspot = null
                        },
                        label =  { Text(room.name, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        leadingIcon =  {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ThemePrimary,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            // Interactive 360 Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isNightMode) Color(0xFF0A1128) else Color(0xFF1E293B))
                    .pointerInput(Unit)  {
                        detectDragGestures  { change, dragAmount ->
                            change.consume()
                            val sensitivity = 0.002f
                            panOffsetPercent = (panOffsetPercent - dragAmount.x * sensitivity).coerceIn(0f, 1f)
                        }
                    }
            )  {
                // Procedural Panoramic Room Rendering with Perspective Depth
                Canvas(modifier = Modifier.fillMaxSize())  {
                    drawPanoramicRoomScene(
                        roomType = currentRoom.roomType,
                        panOffset = panOffsetPercent,
                        isNight = isNightMode
                    )
                }

                // Hotspots Overlay on 360 panorama
                currentRoom.hotspots.forEach  { hotspot ->
                    // Calculate visual X position based on pan offset
                    val effectiveX = (hotspot.xPercent - (panOffsetPercent - 0.5f)).coerceIn(0.05f, 0.95f)
                    val effectiveY = hotspot.yPercent

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                start = (effectiveX * 340).dp,
                                top = (effectiveY * 220).dp
                            )
                    )  {
                        // Pulsing outer ring
                        Box(
                            modifier = Modifier
                                .size((28 * pulseScale).dp)
                                .clip(CircleShape)
                                .background(ThemeSecondary.copy(alpha = 0.35f))
                                .align(Alignment.Center)
                        )
                        // Inner interactive pin
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(ThemeSecondary)
                                .border(2.dp, Color.White, CircleShape)
                                .clickable  {
                                    activeHotspot = if (activeHotspot?.id == hotspot.id) null else hotspot
                                }
                                .align(Alignment.Center),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = hotspot.title,
                                tint = ThemeTextPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Controls Overlay Top Bar (360 Indicator, Day/Night toggle, Room Specs toggle)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Black.copy(alpha = 0.6f)
                    )  {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ThemeSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "360° Interactive Tour (Drag to Pan)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp))  {
                        // Day / Night Toggle
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.size(36.dp)
                        )  {
                            IconButton(
                                onClick =  { isNightMode = !isNightMode },
                                modifier = Modifier.size(36.dp)
                            )  {
                                Icon(
                                    imageVector = if (isNightMode) Icons.Default.Nightlight else Icons.Default.WbSunny,
                                    contentDescription = "Toggle Lighting",
                                    tint = if (isNightMode) Color(0xFFFBBF24) else Color(0xFFFFD166),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Room Dimensions Spec Toggle
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.size(36.dp)
                        )  {
                            IconButton(
                                onClick =  { showSpecsOverlay = !showSpecsOverlay },
                                modifier = Modifier.size(36.dp)
                            )  {
                                Icon(
                                    imageVector = Icons.Default.Straighten,
                                    contentDescription = "Room Measurements",
                                    tint = if (showSpecsOverlay) ThemePrimary else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Active Hotspot Card Popup
                if (activeHotspot != null)  {
                    activeHotspot?.let  { spot ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(12.dp)
                        )  {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(8.dp, RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = ThemeTextPrimary.copy(alpha = 0.95f)),
                                shape = RoundedCornerShape(12.dp)
                            )  {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                )  {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(ThemePrimaryLight),
                                        contentAlignment = Alignment.Center
                                    )  {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = ThemePrimary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f))  {
                                        Text(
                                            text = spot.title,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = spot.description,
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 11.sp
                                        )
                                    }
                                    IconButton(
                                        onClick =  { activeHotspot = null },
                                        modifier = Modifier.size(24.dp)
                                    )  {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Room Measurement Specs Overlay
                if (showSpecsOverlay)  {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )  {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = ThemeTextPrimary.copy(alpha = 0.92f)),
                            shape = RoundedCornerShape(10.dp)
                        )  {
                            Column(modifier = Modifier.padding(10.dp))  {
                                Text(
                                    text = "📐 Verified Laser Measurements",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Dimensions: 14' 6\" x 12' 4\" (Ceiling: 9' 2\")\n• Window: 6' x 4' Soundproof Double Pane\n• High Speed WiFi: 850 Mbps (Tested)",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 10.sp,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Room Features Checklist
            Column(modifier = Modifier.padding(12.dp))  {
                Text(
                    text = currentRoom.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    currentRoom.features.take(3).forEach  { feat ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f)
                        )  {
                            Text(
                                text = "✓ feat",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// Procedural rendering helper for architectural room panoramic layout
private fun DrawScope.drawPanoramicRoomScene(
    roomType: String,
    panOffset: Float,
    isNight: Boolean
)  {
    val width = size.width
    val height = size.height

    val skyTop = if (isNight) Color(0xFF030712) else Color(0xFF60A5FA)
    val skyBottom = if (isNight) Color(0xFF0F172A) else Color(0xFFBAE6FD)
    val floorColor = if (isNight) Color(0xFF1E1B18) else Color(0xFFB45309).copy(alpha = 0.35f)
    val wallColor = if (isNight) Color(0xFF1E293B) else Color(0xFFF1F5F9)
    val accentWood = if (isNight) Color(0xFF332014) else Color(0xFF78350F)

    // 1. Draw Ceiling & Wall Canvas
    drawRect(
        brush = Brush.verticalGradient(listOf(wallColor.copy(alpha = 0.9f), wallColor)),
        size = Size(width, height * 0.72f)
    )

    // 2. Draw Hardwood Flooring with Perspective Planks
    drawRect(
        brush = Brush.verticalGradient(listOf(floorColor, floorColor.copy(alpha = 0.8f))),
        topLeft = Offset(0f, height * 0.70f),
        size = Size(width, height * 0.30f)
    )

    // Flooring perspective lines
    val numPlanks = 8
    for (i in 0..numPlanks)  {
        val startX = (i.toFloat() / numPlanks) * width
        val endX = startX + (panOffset - 0.5f) * 60f
        drawLine(
            color = Color.Black.copy(alpha = 0.15f),
            start = Offset(startX, height * 0.70f),
            end = Offset(endX, height),
            strokeWidth = 1.5f
        )
    }

    // 3. Draw Large Window overlooking City Skyline
    val windowLeft = width * (0.35f - (panOffset - 0.5f) * 0.5f)
    val windowWidth = width * 0.35f
    val windowTop = height * 0.12f
    val windowHeight = height * 0.52f

    // Window Sky background
    drawRect(
        brush = Brush.verticalGradient(listOf(skyTop, skyBottom)),
        topLeft = Offset(windowLeft, windowTop),
        size = Size(windowWidth, windowHeight)
    )

    // City Skyline Silhouette in Window
    val buildingCount = 5
    for (b in 0 until buildingCount)  {
        val bX = windowLeft + (b * (windowWidth / buildingCount))
        val bWidth = (windowWidth / buildingCount) * 0.85f
        val bHeight = windowHeight * (0.3f + (b % 3) * 0.18f)
        val bY = windowTop + windowHeight - bHeight
        drawRect(
            color = if (isNight) Color(0xFF1E293B) else Color(0xFF94A3B8),
            topLeft = Offset(bX, bY),
            size = Size(bWidth, bHeight)
        )
        if (isNight)  {
            // Little glowing windows in buildings
            for (wy in 1..4)  {
                drawCircle(
                    color = Color(0xFFFEF08A),
                    radius = 1.5f,
                    center = Offset(bX + bWidth * 0.5f, bY + wy * 8f)
                )
            }
        }
    }

    // Window Frame
    drawRect(
        color = Color(0xFF475569),
        topLeft = Offset(windowLeft, windowTop),
        size = Size(windowWidth, windowHeight),
        style = Stroke(width = 4f)
    )
    // Cross Panes
    drawLine(
        color = Color(0xFF475569),
        start = Offset(windowLeft + windowWidth / 2, windowTop),
        end = Offset(windowLeft + windowWidth / 2, windowTop + windowHeight),
        strokeWidth = 2.5f
    )
    drawLine(
        color = Color(0xFF475569),
        start = Offset(windowLeft, windowTop + windowHeight * 0.4f),
        end = Offset(windowLeft + windowWidth, windowTop + windowHeight * 0.4f),
        strokeWidth = 2.5f
    )

    // 4. Draw Furniture specific to Room Type
    when (roomType)  {
        "Bedroom" ->  {
            // Bed Frame & Linens
            val bedLeft = width * (0.05f - (panOffset - 0.5f) * 0.4f)
            val bedWidth = width * 0.30f
            val bedTop = height * 0.52f
            val bedHeight = height * 0.22f

            // Headboard
            drawRect(
                color = accentWood,
                topLeft = Offset(bedLeft, bedTop - 25f),
                size = Size(bedWidth, 30f)
            )
            // Mattress & Duvet
            drawRect(
                color = if (isNight) Color(0xFF334155) else Color(0xFFE2E8F0),
                topLeft = Offset(bedLeft + 5f, bedTop),
                size = Size(bedWidth - 10f, bedHeight)
            )
            // Pillow
            drawRect(
                color = Color.White,
                topLeft = Offset(bedLeft + 12f, bedTop + 5f),
                size = Size(bedWidth * 0.35f, 16f)
            )

            // Study Desk & Lamp on right
            val deskLeft = width * (0.75f - (panOffset - 0.5f) * 0.4f)
            val deskWidth = width * 0.22f
            val deskTop = height * 0.55f
            drawRect(
                color = accentWood,
                topLeft = Offset(deskLeft, deskTop),
                size = Size(deskWidth, 10f)
            )
            // Desk legs
            drawLine(color = Color(0xFF334155), start = Offset(deskLeft + 5f, deskTop + 10f), end = Offset(deskLeft + 5f, height * 0.78f), strokeWidth = 3f)
            drawLine(color = Color(0xFF334155), start = Offset(deskLeft + deskWidth - 5f, deskTop + 10f), end = Offset(deskLeft + deskWidth - 5f, height * 0.78f), strokeWidth = 3f)
            // Monitor
            drawRect(color = Color(0xFF0F172A), topLeft = Offset(deskLeft + deskWidth * 0.3f, deskTop - 25f), size = Size(deskWidth * 0.4f, 22f))
        }
        "Bathroom" ->  {
            // Glass Shower Stall & Vanity Mirror
            val vanityLeft = width * (0.10f - (panOffset - 0.5f) * 0.4f)
            drawRect(color = Color(0xFF94A3B8), topLeft = Offset(vanityLeft, height * 0.25f), size = Size(width * 0.25f, height * 0.30f), style = Stroke(width = 3f))
            drawRect(color = Color(0xFFE2E8F0), topLeft = Offset(vanityLeft - 10f, height * 0.58f), size = Size(width * 0.30f, height * 0.18f))
        }
        "Kitchen" ->  {
            // Quartz Countertop & Overhead Cabinets
            val counterTop = height * 0.55f
            drawRect(color = Color(0xFF334155), topLeft = Offset(0f, height * 0.10f), size = Size(width, height * 0.15f))
            drawRect(color = Color(0xFFE2E8F0), topLeft = Offset(0f, counterTop), size = Size(width, height * 0.25f))
        }
        else ->  {
            // Lounge sofa & coffee table
            val sofaLeft = width * (0.15f - (panOffset - 0.5f) * 0.4f)
            drawRect(color = Color(0xFF0E7490), topLeft = Offset(sofaLeft, height * 0.55f), size = Size(width * 0.50f, height * 0.20f))
        }
    }
}
