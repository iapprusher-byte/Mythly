package com.mythly.app.data.local.dao

import androidx.room.*
import com.mythly.app.data.local.entity.StoryEntity
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Query("SELECT * FROM stories ORDER BY datePublished DESC")
    fun getAllStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStoryById(id: String): StoryEntity?

    @Query("SELECT * FROM stories WHERE deity = :deity ORDER BY datePublished DESC")
    fun getStoriesByDeity(deity: Deity): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE epic = :epic ORDER BY datePublished DESC")
    fun getStoriesByEpic(epic: Epic): Flow<List<StoryEntity>>

    @Query("""
        SELECT * FROM stories
        WHERE title LIKE '%' || :query || '%'
        OR content LIKE '%' || :query || '%'
        ORDER BY datePublished DESC
    """)
    fun searchStories(query: String): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isRead = 1 ORDER BY lastReadAt DESC")
    fun getReadStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories WHERE isFavorite = 1 ORDER BY datePublished DESC")
    fun getFavoriteStories(): Flow<List<StoryEntity>>

    @Query("SELECT * FROM stories ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomStory(): StoryEntity?

    @Query("SELECT COUNT(*) FROM stories WHERE isRead = 1")
    suspend fun getReadCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Update
    suspend fun updateStory(story: StoryEntity)

    @Query("UPDATE stories SET isRead = :isRead, lastReadAt = :timestamp WHERE id = :id")
    suspend fun markAsRead(id: String, isRead: Boolean, timestamp: Long)

    @Query("UPDATE stories SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun markAsFavorite(id: String, isFavorite: Boolean)

    @Delete
    suspend fun deleteStory(story: StoryEntity)

    @Query("DELETE FROM stories")
    suspend fun deleteAllStories()
}
