package com.mythly.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reading_sessions")
data class ReadingSessionEntity(
    @PrimaryKey
    val id: String,
    val storyId: String,
    val startedAt: Long,
    val completedAt: Long? = null,
    val readingTimeSeconds: Int = 0,
    val usedAudio: Boolean = false
)
