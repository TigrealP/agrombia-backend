package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MainContainerScreen(
    onLogout: () -> Unit,
    onAddCropClick: () -> Unit,
    onAddReportClick: () -> Unit,
    onCropClick: (Int, String) -> Unit
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Cultivos") },
                    label = { Text("Mis Cultivos") },
                    selected = currentRoute == "crops",
                    onClick = {
                        navController.navigate("crops") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Noticias") },
                    label = { Text("Noticias") },
                    selected = currentRoute == "news",
                    onClick = {
                        navController.navigate("news") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = currentRoute == "profile",
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Reportes") },
                    label = { Text("Reportes") },
                    selected = currentRoute == "reports",
                    onClick = {
                        navController.navigate("reports") {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = "crops") {
                composable("news") {
                    HomeScreen()
                }
                composable("crops") {
                    CropsScreen(
                        onAddClick = onAddCropClick,
                        onCropClick = onCropClick
                    )
                }
                composable("reports") {
                    ReportsScreen(onAddClick = onAddReportClick)
                }
                composable("profile") {
                    ProfileScreen(onLogout)
                }
            }
        }
    }
}
