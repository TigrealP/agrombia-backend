package com.carlos.agrombia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlos.agrombia.ui.screens.LoginScreen
import com.carlos.agrombia.ui.screens.RegisterScreen
import com.carlos.agrombia.ui.screens.MainContainerScreen
import com.carlos.agrombia.ui.screens.CreateCropScreen
import com.carlos.agrombia.ui.screens.ReportsScreen
import com.carlos.agrombia.ui.screens.CreateReportScreen

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.carlos.agrombia.ui.screens.CropDetailScreen
import com.carlos.agrombia.ui.screens.LandingScreen
import com.carlos.agrombia.ui.screens.PublicWeatherScreen

@Composable
fun AgrombiaNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen(
                onLoginClick = { navController.navigate("login") },
                onWeatherClick = { navController.navigate("public_weather") }
            )
        }
        composable("public_weather") {
            PublicWeatherScreen(onBackClick = { navController.popBackStack() })
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("landing") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("main") {
            MainContainerScreen(
                onLogout = {
                    navController.navigate("landing") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                onAddCropClick = {
                    navController.navigate("create_crop")
                },
                onAddReportClick = {
                    navController.navigate("create_report")
                },
                onCropClick = { cropId, cropName ->
                    navController.navigate("crop_detail/$cropId/$cropName")
                }
            )
        }
        composable(
            route = "crop_detail/{cropId}/{cropName}",
            arguments = listOf(
                navArgument("cropId") { type = NavType.IntType },
                navArgument("cropName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val cropId = backStackEntry.arguments?.getInt("cropId") ?: 0
            val cropName = backStackEntry.arguments?.getString("cropName") ?: "Detalle"
            
            CropDetailScreen(
                cropId = cropId,
                cropName = cropName,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("create_crop") {
            CreateCropScreen(
                onBackClick = { navController.popBackStack() },
                onCropCreated = { navController.popBackStack() } 
            )
        }
        composable("create_report") {
            CreateReportScreen(
                onBackClick = { navController.popBackStack() },
                onReportCreated = { navController.popBackStack() }
            )
        }
    }
}
