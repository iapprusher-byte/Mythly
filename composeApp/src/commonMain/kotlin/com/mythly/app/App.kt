package com.mythly.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mythly.app.di.getAppModules
import com.mythly.app.presentation.navigation.MythlyBottomNavBar
import com.mythly.app.presentation.navigation.MythlyNavGraph
import com.mythly.app.presentation.navigation.Screen
import com.mythly.app.presentation.theme.MythlyTheme
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(
        application = {
            modules(getAppModules())
        }
    ) {
        MythlyTheme {
            MythlyApp()
        }
    }
}

@Composable
private fun MythlyApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    // Determine if bottom bar should be shown
    val showBottomBar = when (currentRoute) {
        Screen.Today.route,
        Screen.Library.route,
        Screen.Profile.route -> true
        else -> false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                MythlyBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            // Pop up to the start destination
                            popUpTo(Screen.Today.route) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
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
            MythlyNavGraph(navController = navController)
        }
    }
}
