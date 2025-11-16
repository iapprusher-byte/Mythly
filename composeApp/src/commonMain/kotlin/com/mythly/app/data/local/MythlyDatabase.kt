package com.mythly.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mythly.app.data.local.dao.ReadingSessionDao
import com.mythly.app.data.local.dao.StoryDao
import com.mythly.app.data.local.dao.UserStatsDao
import com.mythly.app.data.local.entity.ReadingSessionEntity
import com.mythly.app.data.local.entity.StoryConverters
import com.mythly.app.data.local.entity.StoryEntity
import com.mythly.app.data.local.entity.UserStatsEntity

@Database(
    entities = [
        StoryEntity::class,
        UserStatsEntity::class,
        ReadingSessionEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(StoryConverters::class)
abstract class MythlyDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun userStatsDao(): UserStatsDao
    abstract fun readingSessionDao(): ReadingSessionDao
}
