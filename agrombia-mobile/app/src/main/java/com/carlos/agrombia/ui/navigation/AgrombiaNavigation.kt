package com.carlos.agrombia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.carlos.agrombia.ui.screens.LoginScreen
import com.carlos.agrombia.ui.screens.RegisterScreen
import com.carlos.agrombia.ui.screens.HomeScreen

@Composable
fun AgrombiaNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
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
                    // Volver al login tras registrarse, o loguear directo
                    navController.popBackStack() 
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("home") {
            HomeScreen()
        }
    }
}
