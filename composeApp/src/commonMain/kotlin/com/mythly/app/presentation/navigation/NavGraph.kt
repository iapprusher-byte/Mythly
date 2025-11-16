package com.mythly.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mythly.app.presentation.screens.LibraryScreen
import com.mythly.app.presentation.screens.ProfileScreen
import com.mythly.app.presentation.screens.StoryReaderScreen
import com.mythly.app.presentation.screens.TodayScreen

@Composable
fun MythlyNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Today.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Today Screen
        composable(Screen.Today.route) {
            TodayScreen(
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryReader.createRoute(storyId))
                }
            )
        }

        // Library Screen
        composable(Screen.Library.route) {
            LibraryScreen(
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryReader.createRoute(storyId))
                }
            )
        }

        // Story Reader Screen
        composable(
            route = Screen.StoryReader.route,
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            StoryReaderScreen(
                storyId = storyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Profile Screen
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
