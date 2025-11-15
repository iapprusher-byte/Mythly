package com.mythly.app.domain.repository

import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.StoryUiState
import kotlinx.coroutines.flow.Flow

interface StoryRepository {
    fun getAllStories(): Flow<List<StoryUiState>>
    suspend fun getStoryById(id: String): StoryUiState?
    fun getStoriesByDeity(deity: Deity): Flow<List<StoryUiState>>
    fun getStoriesByEpic(epic: Epic): Flow<List<StoryUiState>>
    fun searchStories(query: String): Flow<List<StoryUiState>>
    fun getReadStories(): Flow<List<StoryUiState>>
    fun getFavoriteStories(): Flow<List<StoryUiState>>
    suspend fun getTodayStory(): StoryUiState?
    suspend fun markAsRead(storyId: String)
    suspend fun markAsFavorite(storyId: String, isFavorite: Boolean)
    suspend fun loadInitialContent(): Result<Unit>
}
