package com.mythly.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mythly.app.presentation.navigation.MythlyBottomNavBar
import com.mythly.app.presentation.navigation.MythlyNavGraph
import com.mythly.app.presentation.navigation.Screen
import com.mythly.app.presentation.theme.MythlyTheme

@Composable
fun App() {
    // Koin is initialized at platform level:
    // - Android: MythlyApplication.kt
    // - iOS: MainViewController.kt (needs to be added)
    MythlyTheme {
        MythlyApp()
    }
}

@Composable
private fun MythlyApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Track onboarding completion state
    // TODO: Replace with DataStore or SharedPreferences for persistence
    var hasCompletedOnboarding by remember { mutableStateOf(false) }

    // Determine if bottom bar should be shown
    // Hide bottom bar for splash and onboarding screens
    val showBottomBar = when (currentRoute) {
        Screen.Today.route,
        Screen.Library.route,
        Screen.Profile.route -> true
        else -> false
    }

    // Determine start destination based on onboarding status
    val startDestination = if (hasCompletedOnboarding) {
        Screen.Today.route
    } else {
        Screen.Splash.route
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                MythlyBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination and save state
                            popUpTo(Screen.Today.route)
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Apply padding only to the NavGraph content, not the entire scaffold
            MythlyNavGraph(
                navController = navController,
                startDestination = startDestination,
                onOnboardingComplete = {
                    // Mark onboarding as completed
                    hasCompletedOnboarding = true
                    // TODO: Save to persistent storage
                }
            )
        }
    }
}
