package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.data.models.ListingEntity
import com.example.ui.components.AvailabilityBadge
import com.example.ui.components.MapPreviewCard
import com.example.ui.components.PaymentCheckoutDialog
import com.example.ui.components.PriceTierPill
import com.example.ui.components.ReportMoveOutDialog
import com.example.ui.components.ScheduleTourDialog
import com.example.ui.components.VerificationBadgesRow
import com.example.ui.components.VirtualTourViewer
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeSecondaryLight
import com.example.ui.theme.ThemeSurfaceVariant
import com.example.ui.theme.ThemeTextSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.theme.ThemeSuccessLight
import com.example.ui.theme.ThemeSuccess
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    listingId: Long,
    viewModel: RoomFinderViewModel,
    onBack: () -> Unit,
    onNavigateToChat: (Long, String) -> Unit,
    modifier: Modifier = Modifier
)  {
    val listings by viewModel.rawListings.collectAsStateWithLifecycle()
    val listing = listings.find  { it.id == listingId }

    var showMoveOutDialog by remember  { mutableStateOf(false) }
    var showPaymentDialog by remember  { mutableStateOf(false) }
    var showScheduleTourDialog by remember  { mutableStateOf(false) }

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
                    Text(
                        text = listing.roomType,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon =  {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_btn"))  {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions =  {
                    IconButton(
                        onClick =  { viewModel.toggleSaveListing(listing.id, listing.isSaved) },
                        modifier = Modifier.testTag("detail_save_btn")
                    )  {
                        Icon(
                            imageVector = if (listing.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Save",
                            tint = if (listing.isSaved) ThemeError else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar =  {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            )  {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    OutlinedButton(
                        onClick =  { showScheduleTourDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("book_tour_bottom_btn"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ThemePrimary),
                        shape = RoundedCornerShape(10.dp)
                    )  {
                        Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Tour", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick =  { showPaymentDialog = true },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp)
                            .testTag("secure_pay_bottom_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary),
                        shape = RoundedCornerShape(10.dp)
                    )  {
                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Secure Pay / Rent", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    )  { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .testTag("listing_detail_screen")
        )  {
            // Interactive 360 Virtual Tour Viewer at top
            VirtualTourViewer(
                listingId = listing.id,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // Header Info & Status
            Column(modifier = Modifier.padding(horizontal = 16.dp))  {
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

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = listing.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "📍 ${listing.address}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Specs Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    SpecCard(
                        icon = Icons.Default.MeetingRoom,
                        title = "Type",
                        value = listing.roomType,
                        modifier = Modifier.weight(1f)
                    )
                    SpecCard(
                        icon = Icons.Default.Bed,
                        title = "Size",
                        value = "${listing.squareFeet} sq ft",
                        modifier = Modifier.weight(1f)
                    )
                    SpecCard(
                        icon = Icons.Default.Bathtub,
                        title = "Bath",
                        value = if (listing.hasPrivateBath) "Private" else "Shared",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Price Breakdown & Rent Tiers Card (CRITICAL FEATURE)
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
                            Text(
                                text = "Monthly Rent Tiers & Pricing",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = ThemePrimaryLight
                            )  {
                                Text(
                                    text = "Transparent Breakdown",
                                    color = ThemePrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        PriceRow(label = "Base Monthly Rent", amount = "₹${listing.basePrice} / mo")
                        PriceRow(
                            label = "Utilities (WiFi, Water, Heat)",
                            amount = if (listing.utilitiesPrice == 0) "Included in Rent" else "₹${listing.utilitiesPrice} / mo"
                        )
                        PriceRow(label = "Refundable Security Deposit", amount = "₹${listing.depositPrice} (Held in Escrow)")

                        if (listing.studentDiscountPercent > 0)  {
                            PriceRow(
                                label = "Verified Student Discount",
                                amount = "-${listing.studentDiscountPercent}% (.edu)",
                                isHighlight = true
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        val discountedRent = if (listing.studentDiscountPercent > 0)  {
                            listing.basePrice * (100 - listing.studentDiscountPercent) / 100
                        } else  {
                            listing.basePrice
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Column  {
                                Text("Effective Student Rent:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("With valid college ID verification", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Text(
                                text = "₹discountedRent / mo",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = ThemePrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Verified Landlord Profile Card & Direct Messaging CTA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder()
                )  {
                    Column(modifier = Modifier.padding(16.dp))  {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ThemePrimary),
                                contentAlignment = Alignment.Center
                            )  {
                                Text(
                                    text = listing.landlordName.take(1),
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f))  {
                                Text(
                                    text = listing.landlordName,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(verticalAlignment = Alignment.CenterVertically)  {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = ThemeSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        text = "${listing.landlordRating} • ${listing.landlordReviewCount} tenant reviews",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        VerificationBadgesRow(
                            isLandlordVerified = listing.landlordVerified,
                            isBackgroundChecked = listing.backgroundChecked,
                            isBusinessVerified = listing.businessVerified,
                            isOwnershipVerified = listing.ownershipVerified
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick =  { onNavigateToChat(listing.id, listing.title) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("chat_landlord_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemePrimaryDark),
                            shape = RoundedCornerShape(8.dp)
                        )  {
                            Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Direct Message Verified Landlord", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "About this Room",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = listing.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Amenities List
                Text(
                    text = "Amenities & Features",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                listing.amenitiesJson.split(",").forEach  { amenity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(ThemeSuccessLight),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = ThemeSuccess,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = amenity.trim(),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Move-out / Room Vacating Notice Action Card (CRITICAL FEATURE)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ThemeSecondaryLight.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ThemeSecondary))
                )  {
                    Column(modifier = Modifier.padding(14.dp))  {
                        Row(verticalAlignment = Alignment.CenterVertically)  {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = ThemeSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Leaving this Room?",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Currently renting here? Submit your departure notice directly to the owner. We'll automatically trigger move-out vacancy alerts to help other students find a home.",
                            fontSize = 12.sp,
                            color = ThemeTextSecondary,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick =  { showMoveOutDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("report_move_out_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = ThemeError),
                            shape = RoundedCornerShape(8.dp)
                        )  {
                            Text("Inform Landlord of Move-Out", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Location & Google Maps Intent Card (CRITICAL FEATURE)
                MapPreviewCard(listing = listing)

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Dialogs
        if (showMoveOutDialog)  {
            ReportMoveOutDialog(
                listing = listing,
                onDismiss =  { showMoveOutDialog = false },
                onSubmitNotice =  { moveOutDate, reason ->
                    viewModel.submitMoveOutNotice(
                        listingId = listing.id,
                        listingTitle = listing.title,
                        address = listing.address,
                        neighborhood = listing.neighborhood,
                        universityNearby = listing.universityNearby,
                        moveOutDate = moveOutDate,
                        monthlyPrice = listing.basePrice,
                        leavingReason = reason
                    )
                }
            )
        }

        if (showPaymentDialog)  {
            PaymentCheckoutDialog(
                listing = listing,
                onDismiss =  { showPaymentDialog = false },
                onPaymentSuccess =  { amount, type, method ->
                    viewModel.processPayment(
                        listingId = listing.id,
                        propertyTitle = listing.title,
                        amount = amount,
                        paymentType = type,
                        paymentMethod = method
                    )
                }
            )
        }

        if (showScheduleTourDialog)  {
            ScheduleTourDialog(
                listing = listing,
                onDismiss =  { showScheduleTourDialog = false },
                onConfirmTour =  { date, type ->
                    viewModel.sendMessage(
                        listingId = listing.id,
                        listingTitle = listing.title,
                        text = "Hi ${listing.landlordName}, I would like to book a type on date.",
                        tourDate = "type: date"
                    )
                }
            )
        }
    }
}

@Composable
private fun SpecCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
)  {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    )  {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )  {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ThemePrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    amount: String,
    isHighlight: Boolean = false
)  {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    )  {
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isHighlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = amount,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isHighlight) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
        )
    }
}
