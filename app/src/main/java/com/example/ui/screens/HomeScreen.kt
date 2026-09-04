package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.models.PriceCategory
import com.example.ui.components.RoomCard
import com.example.ui.theme.ThemeError
import com.example.ui.theme.ThemeSecondary
import com.example.ui.theme.ThemeTextPrimary
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimaryLight
import com.example.ui.theme.ThemePrimary
import com.example.ui.theme.ThemeSuccess
import com.example.ui.viewmodel.RoomFinderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: RoomFinderViewModel,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToVirtualTour: (Long) -> Unit,
    onNavigateToMoveOutAlerts: () -> Unit,
    modifier: Modifier = Modifier
)  {
    val listings by viewModel.filteredListings.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedPriceCategory by viewModel.selectedPriceCategory.collectAsStateWithLifecycle()
    val selectedRoomType by viewModel.selectedRoomType.collectAsStateWithLifecycle()
    val selectedUniversity by viewModel.selectedUniversityFilter.collectAsStateWithLifecycle()
    val onlyVerified by viewModel.onlyVerifiedLandlords.collectAsStateWithLifecycle()
    val moveOutAlerts by viewModel.moveOutAlerts.collectAsStateWithLifecycle()

    var showFiltersRow by remember  { mutableStateOf(false) }

    val universityOptions = listOf(
        "All Campuses",
        "Srinagar University (HNBGU)",
        "Columbia University",
        "Boston University (BU)",
        "Pratt / Tandon"
    )

    val roomTypeOptions = listOf("All", "Studio Loft", "Private Room", "Shared Room", "Master Suite")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 90.dp)
    )  {
        // Hero Header Banner
        item  {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                ThemePrimaryDark,
                                ThemePrimary,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            )  {
                Column  {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Column  {
                            Text(
                                text = "UrbanRoom",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Student & Urban Room Finder",
                                fontSize = 13.sp,
                                color = ThemePrimaryLight,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Verified Security Shield
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.Black.copy(alpha = 0.35f)
                        )  {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            )  {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = "Verified Platform",
                                    tint = ThemeSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "100% Verified",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Urban Search Input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange =  { viewModel.setSearchQuery(it) },
                        placeholder =  { Text("Search by campus, street, neighborhood...") },
                        leadingIcon =  {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = ThemePrimary)
                        },
                        trailingIcon =  {
                            if (searchQuery.isNotBlank())  {
                                IconButton(onClick =  { viewModel.setSearchQuery("") })  {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = ThemePrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )
                }
            }
        }

        // Move-Out Vacancy Alert Highlight Banner
        if (moveOutAlerts.isNotEmpty())  {
            item  {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable  { onNavigateToMoveOutAlerts() }
                        .testTag("move_out_alert_banner"),
                    colors = CardDefaults.cardColors(containerColor = ThemeError.copy(alpha = 0.10f)),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ThemeError, ThemeSecondary)))
                )  {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    )  {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ThemeError.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        )  {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = ThemeError,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f))  {
                            Text(
                                text = "🔔 ${moveOutAlerts.size} Move-Out Vacancies Alert",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemeError
                            )
                            Text(
                                text = "Students reported upcoming vacancies in Srikot & Morningside Heights.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                        Text(
                            text = "View →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ThemeError
                        )
                    }
                }
            }
        }

        // Pricing Tiers Filter Tabs
        item  {
            Column(modifier = Modifier.padding(top = 10.dp))  {
                Text(
                    text = "Price Category Lists",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    items(PriceCategory.values())  { category ->
                        val isSelected = selectedPriceCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick =  { viewModel.setPriceCategory(category) },
                            label =  {
                                Text(
                                    text = category.label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemePrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Room Type & Campus Filter Chips
        item  {
            Column(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))  {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )  {
                    // Verified Landlord toggle chip
                    item  {
                        FilterChip(
                            selected = onlyVerified,
                            onClick =  { viewModel.toggleVerifiedLandlordsOnly() },
                            label =  { Text("Verified Only", fontSize = 12.sp) },
                            leadingIcon =  {
                                Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(14.dp))
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemeSuccess,
                                selectedLabelColor = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
                    }

                    // Room types
                    items(roomTypeOptions)  { roomType ->
                        val isSelected = selectedRoomType == roomType
                        FilterChip(
                            selected = isSelected,
                            onClick =  { viewModel.setRoomType(roomType) },
                            label =  { Text(roomType, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ThemePrimaryDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // University Campus Selector Row
        item  {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            )  {
                items(universityOptions)  { uni ->
                    val isSelected = selectedUniversity == uni
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ThemePrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(ThemePrimary, ThemePrimaryDark))) else null,
                        modifier = Modifier.clickable  { viewModel.setUniversityFilter(uni) }
                    )  {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        )  {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = if (isSelected) ThemePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = uni,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ThemePrimaryDark else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Section Title & Listings Count
        item  {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )  {
                Text(
                    text = "Available Rooms (${listings.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Real-time updates",
                    fontSize = 11.sp,
                    color = ThemeSuccess,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Listings Feed or Empty State
        if (listings.isEmpty())  {
            item  {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )  {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(ThemePrimaryLight),
                        contentAlignment = Alignment.Center
                    )  {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = ThemePrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No matching rooms found",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Try adjusting your price category or campus filter to see more available student rooms.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        } else  {
            items(listings, key =  { it.id })  { listing ->
                RoomCard(
                    listing = listing,
                    onListingClick =  { onNavigateToDetail(listing.id) },
                    onSaveToggle =  { viewModel.toggleSaveListing(listing.id, listing.isSaved) },
                    onVirtualTourClick =  { onNavigateToVirtualTour(listing.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
