package com.example.profileapp.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.profileapp.ui.screens.LoginScreen
import com.example.profileapp.ui.screens.ProfileScreen
import com.example.profileapp.ui.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
}

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    var justRegistered by remember { mutableStateOf(false) }

    // Limpia el back stack completo y navega a [route]
    fun navigateClearing(route: String) = nav.navigate(route) {
        popUpTo(nav.graph.id) { inclusive = true }
    }

    NavHost(navController = nav, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                justRegistered = justRegistered,
                onLoginSuccess = { justRegistered = false; navigateClearing(Routes.PROFILE) },
                onGoRegister = { nav.navigate(Routes.REGISTER) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = { justRegistered = true; navigateClearing(Routes.LOGIN) },
                onBack = { nav.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(onLogout = { justRegistered = false; navigateClearing(Routes.LOGIN) })
        }
    }
}
