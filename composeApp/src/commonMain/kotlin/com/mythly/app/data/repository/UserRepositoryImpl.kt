package com.mythly.app.data.repository

import com.mythly.app.data.local.dao.ReadingSessionDao
import com.mythly.app.data.local.dao.UserStatsDao
import com.mythly.app.data.local.entity.ReadingSessionEntity
import com.mythly.app.data.local.entity.UserStatsEntity
import com.mythly.app.domain.model.ReadingSession
import com.mythly.app.domain.model.UserStats
import com.mythly.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.math.abs

class UserRepositoryImpl(
    private val userStatsDao: UserStatsDao,
    private val readingSessionDao: ReadingSessionDao
) : UserRepository {

    override fun getUserStats(): Flow<UserStats> =
        userStatsDao.getUserStats().map { entity ->
            entity?.toModel() ?: UserStats()
        }

    override suspend fun updateStreak() {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStatsEntity()
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val oneDayMs = 24 * 60 * 60 * 1000L

        val newStreak = when {
            stats.lastReadDate == null -> 1
            abs(currentTime - stats.lastReadDate) <= oneDayMs -> stats.currentStreak
            abs(currentTime - stats.lastReadDate) <= 2 * oneDayMs -> stats.currentStreak + 1
            else -> 1
        }

        val updatedStats = stats.copy(
            currentStreak = newStreak,
            longestStreak = maxOf(stats.longestStreak, newStreak),
            lastReadDate = currentTime,
            readingStreak7Day = newStreak >= 7,
            readingStreak30Day = newStreak >= 30,
            readingStreak100Day = newStreak >= 100
        )

        userStatsDao.insertUserStats(updatedStats)
    }

    override suspend fun incrementStoriesRead() {
        userStatsDao.incrementStoriesRead()
    }

    override suspend fun updateReadingTime(minutes: Int) {
        val stats = userStatsDao.getUserStatsOnce() ?: UserStatsEntity()
        val updatedStats = stats.copy(
            totalReadingTimeMinutes = stats.totalReadingTimeMinutes + minutes
        )
        userStatsDao.updateUserStats(updatedStats)
    }

    override suspend fun saveReadingSession(session: ReadingSession) {
        val entity = ReadingSessionEntity(
            id = session.id,
            storyId = session.storyId,
            startedAt = session.startedAt,
            completedAt = session.completedAt,
            readingTimeSeconds = session.readingTimeSeconds,
            usedAudio = session.usedAudio
        )
        readingSessionDao.insertSession(entity)
    }

    override fun getRecentSessions(): Flow<List<ReadingSession>> =
        readingSessionDao.getRecentSessions().map { entities ->
            entities.map { it.toModel() }
        }

    private fun UserStatsEntity.toModel() = UserStats(
        currentStreak = currentStreak,
        longestStreak = longestStreak,
        totalStoriesRead = totalStoriesRead,
        lastReadDate = lastReadDate,
        favoriteDeity = favoriteDeity,
        readingStreak7Day = readingStreak7Day,
        readingStreak30Day = readingStreak30Day,
        readingStreak100Day = readingStreak100Day,
        totalReadingTimeMinutes = totalReadingTimeMinutes
    )

    private fun ReadingSessionEntity.toModel() = ReadingSession(
        id = id,
        storyId = storyId,
        startedAt = startedAt,
        completedAt = completedAt,
        readingTimeSeconds = readingTimeSeconds,
        usedAudio = usedAudio
    )
}
