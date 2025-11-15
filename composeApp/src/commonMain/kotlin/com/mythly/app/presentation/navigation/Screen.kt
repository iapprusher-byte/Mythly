package com.mythly.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Today : Screen

    @Serializable
    data object Library : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data class StoryReader(val storyId: String) : Screen
}
