package com.mythly.app.data.local.dao

import androidx.room.*
import com.mythly.app.data.local.entity.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsOnce(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStatsEntity)

    @Update
    suspend fun updateUserStats(stats: UserStatsEntity)

    @Query("UPDATE user_stats SET currentStreak = :streak WHERE id = 1")
    suspend fun updateStreak(streak: Int)

    @Query("UPDATE user_stats SET totalStoriesRead = totalStoriesRead + 1 WHERE id = 1")
    suspend fun incrementStoriesRead()
}
