package com.mythly.app.data.repository

import co.touchlab.kermit.Logger
import com.mythly.app.data.local.dao.StoryDao
import com.mythly.app.data.local.entity.StoryEntity
import com.mythly.app.domain.model.*
import com.mythly.app.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class StoryRepositoryImpl(
    private val storyDao: StoryDao,
    private val jsonContent: String
) : StoryRepository {

    private val logger = Logger.withTag("StoryRepository")

    override fun getAllStories(): Flow<List<StoryUiState>> =
        storyDao.getAllStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override suspend fun getStoryById(id: String): StoryUiState? =
        storyDao.getStoryById(id)?.toUiState()

    override fun getStoriesByDeity(deity: Deity): Flow<List<StoryUiState>> =
        storyDao.getStoriesByDeity(deity).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getStoriesByEpic(epic: Epic): Flow<List<StoryUiState>> =
        storyDao.getStoriesByEpic(epic).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun searchStories(query: String): Flow<List<StoryUiState>> =
        storyDao.searchStories(query).map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getReadStories(): Flow<List<StoryUiState>> =
        storyDao.getReadStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override fun getFavoriteStories(): Flow<List<StoryUiState>> =
        storyDao.getFavoriteStories().map { entities ->
            entities.map { it.toUiState() }
        }

    override suspend fun getTodayStory(): StoryUiState? {
        // Simple algorithm: get random story for now
        // In production, use day-based algorithm
        return storyDao.getRandomStory()?.toUiState()
    }

    override suspend fun markAsRead(storyId: String) {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        storyDao.markAsRead(storyId, true, timestamp)
    }

    override suspend fun markAsFavorite(storyId: String, isFavorite: Boolean) {
        storyDao.markAsFavorite(storyId, isFavorite)
    }

    override suspend fun loadInitialContent(): Result<Unit> {
        return try {
            logger.d { "Loading initial content..." }

            // Check if already loaded
            val existingStories = storyDao.getAllStories().first()
            if (existingStories.isNotEmpty()) {
                logger.i { "Stories already loaded. Count: ${existingStories.size}" }
                return Result.success(Unit)
            }

            // Parse JSON
            val json = Json { ignoreUnknownKeys = true }
            val storiesData = json.decodeFromString<StoriesContainer>(jsonContent)
            logger.i { "Parsed ${storiesData.stories.size} stories from JSON" }

            // Convert to entities
            val entities = storiesData.stories.map { dto ->
                StoryEntity(
                    id = dto.id,
                    title = dto.title,
                    content = dto.content,
                    moralLesson = dto.moralLesson,
                    deity = dto.deity,
                    epic = dto.epic,
                    values = dto.values,
                    imageUrl = dto.imageUrl,
                    readTimeMinutes = dto.readTimeMinutes,
                    datePublished = dto.datePublished,
                    audioUrl = dto.audioUrl,
                    sanskritTitle = dto.sanskritTitle,
                    relatedStoryIds = dto.relatedStoryIds ?: emptyList()
                )
            }

            // Insert into database
            storyDao.insertStories(entities)
            logger.i { "Successfully loaded ${entities.size} stories into database" }

            Result.success(Unit)
        } catch (e: Exception) {
            logger.e(throwable = e) { "Failed to load initial content: ${e.message}" }
            Result.failure(e)
        }
    }

    private fun StoryEntity.toUiState() = StoryUiState(
        story = Story(
            id = id,
            title = title,
            content = content,
            moralLesson = moralLesson,
            deity = deity,
            epic = epic,
            values = values,
            imageUrl = imageUrl,
            readTimeMinutes = readTimeMinutes,
            datePublished = datePublished,
            audioUrl = audioUrl,
            sanskritTitle = sanskritTitle,
            relatedStoryIds = relatedStoryIds
        ),
        isRead = isRead,
        isFavorite = isFavorite,
        lastReadAt = lastReadAt
    )
}

@Serializable
private data class StoriesContainer(
    val stories: List<StoryDto>
)

@Serializable
private data class StoryDto(
    val id: String,
    val title: String,
    val content: String,
    val moralLesson: String,
    val deity: Deity,
    val epic: Epic,
    val values: List<Value>,
    val imageUrl: String,
    val readTimeMinutes: Int,
    val datePublished: Long,
    val audioUrl: String? = null,
    val sanskritTitle: String? = null,
    val relatedStoryIds: List<String>? = null
)
