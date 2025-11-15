package com.mythly.app.data.local.dao

import androidx.room.*
import com.mythly.app.data.local.entity.ReadingSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingSessionDao {
    @Query("SELECT * FROM reading_sessions ORDER BY startedAt DESC LIMIT 50")
    fun getRecentSessions(): Flow<List<ReadingSessionEntity>>

    @Query("SELECT * FROM reading_sessions WHERE storyId = :storyId ORDER BY startedAt DESC")
    fun getSessionsForStory(storyId: String): Flow<List<ReadingSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ReadingSessionEntity)

    @Update
    suspend fun updateSession(session: ReadingSessionEntity)
}
