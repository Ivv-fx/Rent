package com.example

import androidx.compose.runtime.collectAsState
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.AuthViewModelFactory
import com.example.data.repository.FirebaseAuthRepositoryImpl
import com.example.data.local.SessionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.local.AppDatabase
import com.example.data.repository.RoomFinderRepository
import com.example.ui.screens.CommunityForumScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LandlordDashboardScreen
import com.example.ui.screens.ListingDetailScreen
import com.example.ui.screens.MessagesScreen
import com.example.ui.screens.MoveOutAlertsScreen
import com.example.ui.screens.ProfileVerificationScreen
import com.example.ui.screens.SavedRoomsScreen
import com.example.ui.screens.VirtualTourViewerScreen
import com.example.ui.theme.ThemeError
import com.example.ui.theme.UrbanRoomTheme
import com.example.ui.theme.ThemePrimaryDark
import com.example.ui.theme.ThemePrimary
import com.example.ui.viewmodel.RoomFinderViewModel
import com.example.ui.viewmodel.RoomFinderViewModelFactory

import androidx.compose.animation.Crossfade
import com.example.ui.screens.AuthScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val tag: String)  {
    object Auth : Screen("auth", "Auth", Icons.Default.Person, "nav_auth")
    object Home : Screen("home", "Explore", Icons.Default.Search, "nav_explore")
    object MoveOutAlerts : Screen("move_out_alerts", "Alerts", Icons.Default.NotificationsActive, "nav_alerts")
    object Landlord : Screen("landlord_dashboard", "Landlord Pro", Icons.Default.Dashboard, "nav_landlord")
    object Community : Screen("community", "Community", Icons.Default.Forum, "nav_community")
    object Profile : Screen("profile", "Profile", Icons.Default.Person, "nav_profile")
}

class MainActivity : ComponentActivity()  {

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)
        val repository = RoomFinderRepository(database)
        val viewModelFactory = RoomFinderViewModelFactory(repository)
        val viewModel: RoomFinderViewModel by viewModels { viewModelFactory }
        val sessionManager = SessionManager(applicationContext)
        val authRepository = FirebaseAuthRepositoryImpl()
        val authViewModelFactory = AuthViewModelFactory(authRepository, sessionManager)
        val authViewModel: AuthViewModel by viewModels { authViewModelFactory }



        setContent  {
            UrbanRoomTheme  {
                MainAppNavHost(viewModel = viewModel, authViewModel = authViewModel)
            }
        }
    }
}

@Composable
fun MainAppNavHost(viewModel: RoomFinderViewModel, authViewModel: AuthViewModel)  {
    val loggedInUserId by authViewModel.isLoggedIn.collectAsState(initial = null)
    val isLoggedIn = loggedInUserId != null

    Crossfade(targetState = isLoggedIn, label = "AuthCrossfade")  { loggedIn ->
        if (loggedIn)  {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val alerts by viewModel.moveOutAlerts.collectAsStateWithLifecycle()

            val bottomNavItems = listOf(
                Screen.Home,
                Screen.MoveOutAlerts,
                Screen.Landlord,
                Screen.Community,
                Screen.Profile
            )

            val showBottomBar = currentRoute in bottomNavItems.map  { it.route }

            Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar =  {
            AnimatedVisibility(visible = showBottomBar)  {
                NavigationBar(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                )  {
                    bottomNavItems.forEach  { screen ->
                        val selected = currentRoute == screen.route

                        NavigationBarItem(
                            selected = selected,
                            onClick =  {
                                if (currentRoute != screen.route)  {
                                    navController.navigate(screen.route)  {
                                        popUpTo(navController.graph.findStartDestination().id)  {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon =  {
                                if (screen == Screen.MoveOutAlerts && alerts.isNotEmpty())  {
                                    BadgedBox(badge =  {
                                        Badge(containerColor = ThemeError)  {
                                            Text("${alerts.size}", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    })  {
                                        Icon(
                                            imageVector = screen.icon,
                                            contentDescription = screen.title,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                } else  {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            },
                            label =  {
                                Text(
                                    text = screen.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ThemePrimary,
                                selectedTextColor = ThemePrimary,
                                indicatorColor = ThemePrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(screen.tag)
                        )
                    }
                }
            }
        }
    )  { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        )  {
            composable(Screen.Home.route)  {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToDetail =  { listingId ->
                        navController.navigate("listing_detail/$listingId")
                    },
                    onNavigateToVirtualTour =  { listingId ->
                        navController.navigate("virtual_tour/$listingId")
                    },
                    onNavigateToMoveOutAlerts =  {
                        navController.navigate(Screen.MoveOutAlerts.route)
                    }
                )
            }

            composable(Screen.MoveOutAlerts.route)  {
                MoveOutAlertsScreen(
                    viewModel = viewModel,
                    onNavigateToDetail =  { listingId ->
                        navController.navigate("listing_detail/$listingId")
                    }
                )
            }

            composable(Screen.Landlord.route)  {
                LandlordDashboardScreen(
                    viewModel = viewModel,
                    onNavigateToDetail =  { listingId ->
                        navController.navigate("listing_detail/$listingId")
                    }
                )
            }

            composable(Screen.Community.route)  {
                CommunityForumScreen(
                    viewModel = viewModel
                )
            }

            composable(Screen.Profile.route)  {
                ProfileVerificationScreen(
                    viewModel = viewModel,
                    onNavigateToSavedRooms =  {
                        navController.navigate("saved_rooms")
                    }
                )
            }

            composable("saved_rooms")  {
                SavedRoomsScreen(
                    viewModel = viewModel,
                    onBack =  { navController.popBackStack() },
                    onNavigateToDetail =  { listingId ->
                        navController.navigate("listing_detail/$listingId")
                    },
                    onNavigateToVirtualTour =  { listingId ->
                        navController.navigate("virtual_tour/$listingId")
                    }
                )
            }

            composable(
                route = "listing_detail/{listingId}",
                arguments = listOf(navArgument("listingId")  { type = NavType.LongType })
            )  { backStackEntry ->
                val listingId = backStackEntry.arguments?.getLong("listingId") ?: 1L
                ListingDetailScreen(
                    listingId = listingId,
                    viewModel = viewModel,
                    onBack =  { navController.popBackStack() },
                    onNavigateToChat =  { id, title ->
                        navController.navigate("messages")
                    }
                )
            }

            composable(
                route = "virtual_tour/{listingId}",
                arguments = listOf(navArgument("listingId")  { type = NavType.LongType })
            )  { backStackEntry ->
                val listingId = backStackEntry.arguments?.getLong("listingId") ?: 1L
                VirtualTourViewerScreen(
                    listingId = listingId,
                    viewModel = viewModel,
                    onBack =  { navController.popBackStack() },
                    onNavigateToDetail =  { id ->
                        navController.navigate("listing_detail/$id")
                    },
                    onNavigateToChat =  { id, title ->
                        navController.navigate("messages")
                    }
                )
            }

            composable("messages")  {
                MessagesScreen(
                    viewModel = viewModel,
                    onNavigateToDetail =  { listingId ->
                        navController.navigate("listing_detail/$listingId")
                    }
                )
            }
        }
    }
        } else  {
            AuthScreen(viewModel = authViewModel, onAuthSuccess = { /* No-op, state triggers re-compose */ })
        }
    }
}
