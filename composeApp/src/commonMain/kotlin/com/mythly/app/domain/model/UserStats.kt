package com.mythly.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStats(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalStoriesRead: Int = 0,
    val lastReadDate: Long? = null,
    val favoriteDeity: Deity? = null,
    val readingStreak7Day: Boolean = false,
    val readingStreak30Day: Boolean = false,
    val readingStreak100Day: Boolean = false,
    val totalReadingTimeMinutes: Int = 0
)

@Serializable
data class ReadingSession(
    val id: String,
    val storyId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val readingTimeSeconds: Int = 0,
    val usedAudio: Boolean = false
)
