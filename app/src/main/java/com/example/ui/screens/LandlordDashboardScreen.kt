package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.MaintenanceTicketEntity
import com.example.data.models.PaymentRecordEntity
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeErrorLight
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
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordDashboardScreen(
    viewModel: RoomFinderViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
)  {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val maintenanceTickets by viewModel.maintenanceTickets.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val listings by viewModel.rawListings.collectAsStateWithLifecycle()

    var selectedTabIndex by remember  { mutableIntStateOf(0) }
    val tabTitles = listOf("Maintenance", "Payments", "Subscription", "My Listings")

    var showCreateTicketDialog by remember  { mutableStateOf(false) }
    var showAddListingDialog by remember  { mutableStateOf(false) }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Column  {
                        Row(verticalAlignment = Alignment.CenterVertically)  {
                            Text(
                                text = "Landlord Pro Portal",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = ThemePrimary
                            )  {
                                Text(
                                    text = userProfile.landlordSubscription,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Manage listings, maintenance & subscription",
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
                    onClick =  { showCreateTicketDialog = true },
                    containerColor = ThemePrimary,
                    contentColor = Color.White,
                    icon =  { Icon(Icons.Default.Build, contentDescription = null) },
                    text =  { Text("New Ticket", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("new_maintenance_fab")
                )
            } else if (selectedTabIndex == 3)  {
                ExtendedFloatingActionButton(
                    onClick =  { showAddListingDialog = true },
                    containerColor = ThemePrimary,
                    contentColor = Color.White,
                    icon =  { Icon(Icons.Default.Add, contentDescription = null) },
                    text =  { Text("Add Listing", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("add_listing_fab")
                )
            }
        }
    )  { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("landlord_dashboard")
        )  {
            // High level landlord metrics card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = ThemePrimaryDark),
                shape = RoundedCornerShape(16.dp)
            )  {
                Column(modifier = Modifier.padding(16.dp))  {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Column  {
                            Text(
                                text = "Rajesh Negi (Owner)",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Verified Landlord Pro • ID & Background Passed",
                                color = ThemePrimaryLight,
                                fontSize = 11.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.Black.copy(alpha = 0.3f)
                        )  {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            )  {
                                Icon(Icons.Default.Star, contentDescription = null, tint = ThemeSecondary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("4.9 (38)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )  {
                        MetricItem(title = "Monthly Payout", value = "₹3,950", color = ThemeSuccess)
                        MetricItem(title = "Occupancy", value = "96%", color = Color.White)
                        MetricItem(title = "Active Units", value = "${listings.size}", color = Color.White)
                        MetricItem(title = "Open Tickets", value = "${maintenanceTickets.count { it.status != "Resolved" }}", color = ThemeError)
                    }
                }
            }

            // Tab Bar Navigation
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = ThemePrimary
            )  {
                tabTitles.forEachIndexed  { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick =  { selectedTabIndex = index },
                        text =  {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTabIndex)  {
                0 -> MaintenanceRequestsTab(
                    tickets = maintenanceTickets,
                    onStatusUpdate =  { ticketId, status -> viewModel.updateMaintenanceStatus(ticketId, status) }
                )
                1 -> PaymentHistoryTab(payments = payments)
                2 -> LandlordSubscriptionTab(
                    currentTier = userProfile.landlordSubscription,
                    onUpgrade =  { tier -> viewModel.updateSubscriptionTier(tier) }
                )
                3 -> MyListingsTab(
                    listings = listings,
                    onListingClick = onNavigateToDetail
                )
            }
        }

        // Modals
        if (showCreateTicketDialog)  {
            CreateMaintenanceTicketDialog(
                onDismiss =  { showCreateTicketDialog = false },
                onSubmit =  { unit, issue, desc, cat, urg ->
                    viewModel.createMaintenanceTicket(
                        listingId = 1,
                        propertyTitle = "Sunlit Studio Loft near HNBGU",
                        unitNumber = unit,
                        issueTitle = issue,
                        description = desc,
                        category = cat,
                        urgency = urg
                    )
                }
            )
        }

        if (showAddListingDialog)  {
            AddNewListingDialog(
                onDismiss =  { showAddListingDialog = false },
                onSubmit =  { title, desc, type, address, neigh, uni, walk, base, util, dep, disc, sqft, furn, bath, amen ->
                    viewModel.addLandlordListing(
                        title = title,
                        description = desc,
                        roomType = type,
                        address = address,
                        neighborhood = neigh,
                        universityNearby = uni,
                        walkMinutesToCampus = walk,
                        basePrice = base,
                        utilitiesPrice = util,
                        depositPrice = dep,
                        studentDiscountPercent = disc,
                        squareFeet = sqft,
                        furnished = furn,
                        hasPrivateBath = bath,
                        amenities = amen
                    )
                }
            )
        }
    }
}

@Composable
private fun MetricItem(title: String, value: String, color: Color)  {
    Column(horizontalAlignment = Alignment.CenterHorizontally)  {
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(text = title, fontSize = 10.sp, color = ThemePrimaryLight)
    }
}

// 1. Maintenance Requests Tab
@Composable
private fun MaintenanceRequestsTab(
    tickets: List<MaintenanceTicketEntity>,
    onStatusUpdate: (Long, String) -> Unit
)  {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        item  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Text(
                    text = "Tenant Maintenance Tickets ({tickets.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Auto-dispatch enabled",
                    fontSize = 11.sp,
                    color = ThemeSuccess,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(tickets, key =  { it.id })  { ticket ->
            MaintenanceTicketCard(ticket = ticket, onStatusUpdate = onStatusUpdate)
        }
    }
}

@Composable
private fun MaintenanceTicketCard(
    ticket: MaintenanceTicketEntity,
    onStatusUpdate: (Long, String) -> Unit
)  {
    val isResolved = ticket.status == "Resolved"
    val isProgress = ticket.status == "In Progress"

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
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (ticket.urgency)  {
                        "Emergency", "High" -> ThemeErrorLight
                        else -> ThemeSecondaryLight
                    }
                )  {
                    Text(
                        text = "🚨 ${ticket.urgency} Urgency",
                        color = when (ticket.urgency)  {
                            "Emergency", "High" -> ThemeError
                            else -> ThemeSecondary
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isResolved) ThemeSuccessLight else if (isProgress) ThemeSecondaryLight else ThemeSurfaceVariant
                )  {
                    Text(
                        text = ticket.status,
                        color = if (isResolved) ThemeSuccess else if (isProgress) ThemeSecondary else ThemeTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticket.issueTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "📍 ${ticket.propertyTitle} (Unit ${ticket.unitNumber})",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = ticket.description,
                fontSize = 12.sp,
                color = ThemeTextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            )  {
                if (!isProgress && !isResolved)  {
                    OutlinedButton(
                        onClick =  { onStatusUpdate(ticket.id, "In Progress") },
                        modifier = Modifier.height(34.dp),
                        shape = RoundedCornerShape(6.dp)
                    )  {
                        Text("Mark In Progress", fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                if (!isResolved)  {
                    Button(
                        onClick =  { onStatusUpdate(ticket.id, "Resolved") },
                        modifier = Modifier.height(34.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ThemeSuccess),
                        shape = RoundedCornerShape(6.dp)
                    )  {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Resolve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// 2. Payment History Tab
@Composable
private fun PaymentHistoryTab(payments: List<PaymentRecordEntity>)  {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )  {
        item  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Text(
                    text = "Payment Ledger & Escrow Records",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Direct ACH Deposit",
                    fontSize = 11.sp,
                    color = ThemePrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(payments, key =  { it.id })  { record ->
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
                    Row(verticalAlignment = Alignment.CenterVertically)  {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ThemePrimaryLight),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = ThemePrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column  {
                            Text(
                                text = record.paymentType,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${record.tenantName} • ${record.date}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = record.transactionRef,
                                fontSize = 10.sp,
                                color = ThemePrimary
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End)  {
                        Text(
                            text = "+₹${String.format("%.2f", record.amount)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = ThemeSuccess
                        )
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ThemeSuccessLight
                        )  {
                            Text(
                                text = record.status,
                                color = ThemeSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. Landlord Running Subscription Tab
@Composable
private fun LandlordSubscriptionTab(
    currentTier: String,
    onUpgrade: (String) -> Unit
)  {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    )  {
        Text(
            text = "App Running Subscriptions for Landlords",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Choose an automated management plan with escrow handling, 360 virtual tours, and tenant background verification.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
        )

        // Free Starter Tier
        SubscriptionPlanCard(
            title = "Starter Landlord",
            price = "Free",
            billingSubtext = "Single property basics",
            features = listOf(
                "Up to 1 active room listing",
                "Standard messaging with students",
                "Basic payment tracking ledger"
            ),
            isCurrent = currentTier == "Free",
            onSelect =  { onUpgrade("Free") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Pro Landlord Tier (Featured)
        SubscriptionPlanCard(
            title = "Pro Landlord",
            price = "₹19.99 / mo",
            billingSubtext = "Most popular for urban student properties",
            features = listOf(
                "Unlimited 360° Interactive Virtual Tours",
                "Automated Move-Out Vacancy Alert push blasts",
                "Integrated Tenant Background & Credit Checks",
                "Instant ACH escrow rent payouts (Next Day)",
                "Maintenance ticket dispatch tracking"
            ),
            isCurrent = currentTier == "Pro Landlord",
            isPopular = true,
            onSelect =  { onUpgrade("Pro Landlord") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Enterprise Urban Tier
        SubscriptionPlanCard(
            title = "Enterprise Urban",
            price = "₹49.99 / mo",
            billingSubtext = "For building managers & syndicates",
            features = listOf(
                "Everything in Pro Landlord",
                "Multi-property syndication & automated marketing",
                "24/7 Dedicated emergency dispatch hotline",
                "Integrated CPA Tax Ledger & 1099 automation"
            ),
            isCurrent = currentTier == "Enterprise Urban",
            onSelect =  { onUpgrade("Enterprise Urban") }
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun SubscriptionPlanCard(
    title: String,
    price: String,
    billingSubtext: String,
    features: List<String>,
    isCurrent: Boolean,
    isPopular: Boolean = false,
    onSelect: () -> Unit
)  {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                if (isCurrent) 2.dp else if (isPopular) 1.5.dp else 1.dp,
                if (isCurrent) ThemePrimary else if (isPopular) ThemeSecondary else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(14.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) ThemePrimaryLight.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp)
    )  {
        Column(modifier = Modifier.padding(16.dp))  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Column  {
                    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = billingSubtext, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (isPopular)  {
                    Surface(shape = RoundedCornerShape(4.dp), color = ThemeSecondary)  {
                        Text(
                            text = "POPULAR",
                            color = ThemeTextPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = price,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ThemePrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            features.forEach  { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ThemeSuccess,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = feature, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCurrent) ThemeSuccess else ThemePrimary
                ),
                shape = RoundedCornerShape(8.dp)
            )  {
                Text(
                    text = if (isCurrent) "✓ Active Subscription" else "Select Plan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// 4. My Listings Tab
@Composable
private fun MyListingsTab(
    listings: List<com.example.data.models.ListingEntity>,
    onListingClick: (Long) -> Unit
)  {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )  {
        items(listings, key =  { it.id })  { listing ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable  { onListingClick(listing.id) },
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
                    Column(modifier = Modifier.weight(1f))  {
                        Text(text = listing.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(text = "${listing.roomType} • ${listing.squareFeet} sq ft", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = listing.address, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column(horizontalAlignment = Alignment.End)  {
                        Text(text = "₹${listing.basePrice}/mo", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = ThemePrimary)
                        Text(text = listing.availabilityStatus, fontSize = 10.sp, color = ThemeSuccess, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Dialog: Create Maintenance Ticket
@Composable
private fun CreateMaintenanceTicketDialog(
    onDismiss: () -> Unit,
    onSubmit: (unit: String, issue: String, desc: String, category: String, urgency: String) -> Unit
)  {
    var unitNumber by remember  { mutableStateOf("3B") }
    var issueTitle by remember  { mutableStateOf("") }
    var description by remember  { mutableStateOf("") }
    var category by remember  { mutableStateOf("Plumbing") }
    var urgency by remember  { mutableStateOf("Medium") }

    Dialog(onDismissRequest = onDismiss)  {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )  {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            )  {
                Text(
                    text = "Submit Maintenance Request",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = unitNumber,
                    onValueChange =  { unitNumber = it },
                    label =  { Text("Unit / Room Number") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = issueTitle,
                    onValueChange =  { issueTitle = it },
                    label =  { Text("Issue Title (e.g. Bathroom Leak, AC repair)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange =  { description = it },
                    label =  { Text("Detailed Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Urgency selector
                Text("Urgency Level", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    listOf("Low", "Medium", "High", "Emergency").forEach  { urg ->
                        val isSelected = urgency == urg
                        FilterChip(
                            selected = isSelected,
                            onClick =  { urgency = urg },
                            label =  { Text(urg, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (urg == "Emergency") ThemeError else ThemePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

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
                            if (issueTitle.isNotBlank())  {
                                onSubmit(unitNumber, issueTitle, description, category, urgency)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                    )  {
                        Text("Submit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Dialog: Add New Listing
@Composable
private fun AddNewListingDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        description: String,
        roomType: String,
        address: String,
        neighborhood: String,
        universityNearby: String,
        walkMinutesToCampus: Int,
        basePrice: Int,
        utilitiesPrice: Int,
        depositPrice: Int,
        studentDiscountPercent: Int,
        squareFeet: Int,
        furnished: Boolean,
        hasPrivateBath: Boolean,
        amenities: String
    ) -> Unit
)  {
    var title by remember  { mutableStateOf("") }
    var description by remember  { mutableStateOf("") }
    var roomType by remember  { mutableStateOf("Private Room") }
    var state by remember  { mutableStateOf("Maharashtra") }
    var district by remember  { mutableStateOf("Pune") }
    var streetLocation by remember  { mutableStateOf("FC Road") }
    var village by remember  { mutableStateOf("Shivajinagar") }
    var houseName by remember  { mutableStateOf("Apt 4B") }
    var neighborhood by remember  { mutableStateOf("Deccan Gymkhana") }
    var universityNearby by remember  { mutableStateOf("Fergusson College") }
    var walkMinutes by remember  { mutableStateOf("6") }
    var basePrice by remember  { mutableStateOf("780") }
    var utilPrice by remember  { mutableStateOf("40") }
    var depositPrice by remember  { mutableStateOf("500") }
    var studentDiscount by remember  { mutableStateOf("5") }
    var sqft by remember  { mutableStateOf("160") }
    var furnished by remember  { mutableStateOf(true) }
    var privateBath by remember  { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss)  {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        )  {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            )  {
                Text(
                    text = "Post New Student Room Listing",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange =  { title = it },
                    label =  { Text("Listing Title") },
                    placeholder =  { Text("e.g. Cozy Private Bedroom near HNBGU") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = houseName,
                    onValueChange =  { houseName = it },
                    label =  { Text("House Name / Flat No") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    OutlinedTextField(
                        value = streetLocation,
                        onValueChange =  { streetLocation = it },
                        label =  { Text("Nearby Street") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = village,
                        onValueChange =  { village = it },
                        label =  { Text("Village / Locality") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    OutlinedTextField(
                        value = district,
                        onValueChange =  { district = it },
                        label =  { Text("District") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state,
                        onValueChange =  { state = it },
                        label =  { Text("State") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    OutlinedTextField(
                        value = basePrice,
                        onValueChange =  { basePrice = it },
                        label =  { Text("Monthly Rent (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = utilPrice,
                        onValueChange =  { utilPrice = it },
                        label =  { Text("Utils (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                )  {
                    OutlinedTextField(
                        value = walkMinutes,
                        onValueChange =  { walkMinutes = it },
                        label =  { Text("Walk Min to Campus") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = studentDiscount,
                        onValueChange =  { studentDiscount = it },
                        label =  { Text("Student Disc %") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange =  { description = it },
                    label =  { Text("Description & House Rules") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Checkbox(checked = furnished, onCheckedChange =  { furnished = it })
                    Text("Furnished Room", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Checkbox(checked = privateBath, onCheckedChange =  { privateBath = it })
                    Text("Private Bath", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

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
                                onSubmit(
                                    title,
                                    description.ifBlank  { "Modern urban room ideal for students." },
                                    roomType,
                                    "${houseName}, ${streetLocation}, ${village}, ${district}, ${state}, India",
                                    neighborhood,
                                    universityNearby,
                                    walkMinutes.toIntOrNull() ?: 5,
                                    basePrice.toIntOrNull() ?: 750,
                                    utilPrice.toIntOrNull() ?: 35,
                                    depositPrice.toIntOrNull() ?: 500,
                                    studentDiscount.toIntOrNull() ?: 5,
                                    sqft.toIntOrNull() ?: 150,
                                    furnished,
                                    privateBath,
                                    "High Speed WiFi, Keyless Entry, Furnished Study Desk, Laundry in Building"
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1.3f),
                        colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary)
                    )  {
                        Text("Publish Listing", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
