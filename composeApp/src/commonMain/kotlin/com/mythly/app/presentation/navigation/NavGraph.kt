package com.mythly.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mythly.app.presentation.screens.LibraryScreen
import com.mythly.app.presentation.screens.ProfileScreen
import com.mythly.app.presentation.screens.StoryReaderScreen
import com.mythly.app.presentation.screens.TodayScreen
import com.mythly.app.presentation.screens.onboarding.OnboardingScreen
import com.mythly.app.presentation.screens.onboarding.SplashScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MythlyNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route,
    onOnboardingComplete: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Splash Screen
        composable(Screen.Splash.route) {
            val coroutineScope = rememberCoroutineScope()

            SplashScreen(
                onSplashComplete = {
                    // Delay for 2 seconds to show splash screen
                    coroutineScope.launch {
                        delay(2000)
                        // Navigate to onboarding or main based on user preference
                        // For now, always go to onboarding
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )

            // Auto-trigger splash complete
            LaunchedEffect(Unit) {
                delay(2000)
                navController.navigate(Screen.Onboarding.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }

        // Onboarding Screen
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    onOnboardingComplete()
                    navController.navigate(Screen.Today.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onSkip = {
                    onOnboardingComplete()
                    navController.navigate(Screen.Today.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

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
