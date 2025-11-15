package com.mythly.app.domain.repository

import com.mythly.app.domain.model.ReadingSession
import com.mythly.app.domain.model.UserStats
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserStats(): Flow<UserStats>
    suspend fun updateStreak()
    suspend fun incrementStoriesRead()
    suspend fun updateReadingTime(minutes: Int)
    suspend fun saveReadingSession(session: ReadingSession)
    fun getRecentSessions(): Flow<List<ReadingSession>>
}
