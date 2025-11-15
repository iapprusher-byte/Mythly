package com.mythly.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Today : Screen("today")
    data object Library : Screen("library")
    data object Profile : Screen("profile")
    data object StoryReader : Screen("story/{storyId}") {
        fun createRoute(storyId: String) = "story/$storyId"
    }
}

