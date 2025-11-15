package com.mythly.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mythly.app.domain.model.Deity

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Int = 1, // Single row
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
