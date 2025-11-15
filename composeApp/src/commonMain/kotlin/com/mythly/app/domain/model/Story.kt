package com.mythly.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Story(
    val id: String,
    val title: String,
    val content: String,
    val moralLesson: String,
    val deity: Deity,
    val epic: Epic,
    val values: List<Value>,
    val imageUrl: String,
    val readTimeMinutes: Int,
    val datePublished: Long,
    val audioUrl: String? = null,
    val sanskritTitle: String? = null,
    val relatedStoryIds: List<String> = emptyList()
)

// UI-specific wrapper
data class StoryUiState(
    val story: Story,
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val lastReadAt: Long? = null
)
