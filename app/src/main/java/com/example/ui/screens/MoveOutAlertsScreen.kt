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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.MoveOutAlertEntity
import com.example.ui.components.ReportMoveOutDialog
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeErrorLight
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
fun MoveOutAlertsScreen(
    viewModel: RoomFinderViewModel,
    onNavigateToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier
)  {
    val alerts by viewModel.moveOutAlerts.collectAsStateWithLifecycle()
    val listings by viewModel.rawListings.collectAsStateWithLifecycle()
    var selectedFilter by remember  { mutableStateOf("All Neighborhoods") }
    var showReportDialog by remember  { mutableStateOf(false) }

    val filterOptions = listOf("All Neighborhoods", "Srikot", "Morningside Heights", "Fenway / Kenmore")

    val filteredAlerts = alerts.filter  {
        selectedFilter == "All Neighborhoods" || it.neighborhood.contains(selectedFilter, ignoreCase = true)
    }

    Scaffold(
        topBar =  {
            TopAppBar(
                title =  {
                    Column  {
                        Text(
                            text = "Move-Out Vacancy Alerts",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Real-time upcoming room departures",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        floatingActionButton =  {
            ExtendedFloatingActionButton(
                onClick =  { showReportDialog = true },
                containerColor = ThemeError,
                contentColor = Color.White,
                icon =  { Icon(Icons.Default.Add, contentDescription = null) },
                text =  { Text("Report Leaving Room", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("report_leaving_fab")
            )
        }
    )  { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("move_out_alerts_list"),
            contentPadding = PaddingValues(bottom = 90.dp)
        )  {
            // Explanation Banner
            item  {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ThemeError.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ThemeError, ThemeSecondary)))
                )  {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(ThemeError.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = ThemeError,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f))  {
                            Text(
                                text = "Early Bird Move-Out Alerts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeError
                            )
                            Text(
                                text = "When tenants submit their planned departure notice, we notify landlords and push instant vacancy alerts to students seeking housing before the room hits public markets.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Neighborhood Filter Row
            item  {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    items(filterOptions)  { filter ->
                        val isSelected = selectedFilter == filter
                        FilterChip(
                            selected = isSelected,
                            onClick =  { selectedFilter = filter },
                            label =  { Text(filter, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            item  {
                Text(
                    text = "Upcoming Vacancies (${filteredAlerts.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            items(filteredAlerts, key =  { it.id })  { alert ->
                MoveOutAlertCard(
                    alert = alert,
                    onToggleSubscription =  { viewModel.toggleMoveOutSubscription(alert.id, alert.isSubscribed) },
                    onViewListing =  { onNavigateToDetail(alert.listingId) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        if (showReportDialog)  {
            val firstListing = listings.firstOrNull() ?: com.example.data.sample.SampleData.sampleListings.first()
            ReportMoveOutDialog(
                listing = firstListing,
                onDismiss =  { showReportDialog = false },
                onSubmitNotice =  { moveOutDate, reason ->
                    viewModel.submitMoveOutNotice(
                        listingId = firstListing.id,
                        listingTitle = firstListing.title,
                        address = firstListing.address,
                        neighborhood = firstListing.neighborhood,
                        universityNearby = firstListing.universityNearby,
                        moveOutDate = moveOutDate,
                        monthlyPrice = firstListing.basePrice,
                        leavingReason = reason
                    )
                }
            )
        }
    }
}

@Composable
fun MoveOutAlertCard(
    alert: MoveOutAlertEntity,
    onToggleSubscription: () -> Unit,
    onViewListing: () -> Unit,
    modifier: Modifier = Modifier
)  {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    )  {
        Column(modifier = Modifier.padding(14.dp))  {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            )  {
                Row(verticalAlignment = Alignment.CenterVertically)  {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = ThemeErrorLight
                    )  {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = ThemeError,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Vacating: ${alert.moveOutDate}",
                                color = ThemeError,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Text(
                    text = "₹${alert.monthlyPrice}/mo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ThemePrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = alert.listingTitle,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically)  {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = ThemePrimary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Near ${alert.universityNearby} (${alert.neighborhood})",
                    fontSize = 12.sp,
                    color = ThemePrimaryDark,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Departing student quote box
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )  {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically)  {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column  {
                        Text(
                            text = "${alert.departingTenantName}:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "\"${alert.leavingReason}\"",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                // Subscription Button
                OutlinedButton(
                    onClick = onToggleSubscription,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (alert.isSubscribed) ThemeSuccess else ThemeError
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(38.dp)
                )  {
                    Icon(
                        imageVector = if (alert.isSubscribed) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (alert.isSubscribed) "Subscribed (${alert.subscribersCount})" else "Notify Me (${alert.subscribersCount})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onViewListing,
                    colors = ButtonDefaults.buttonColors(containerColor = ThemePrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(38.dp)
                )  {
                    Text("View Room", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
