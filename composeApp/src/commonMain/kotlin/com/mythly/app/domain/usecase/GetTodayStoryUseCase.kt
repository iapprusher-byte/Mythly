package com.mythly.app.domain.usecase

import co.touchlab.kermit.Logger
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository

class GetTodayStoryUseCase(
    private val storyRepository: StoryRepository
) {
    private val logger = Logger.withTag("GetTodayStoryUseCase")

    suspend operator fun invoke(): Result<StoryUiState> = try {
        logger.d { "Fetching today's story..." }
        val story = storyRepository.getTodayStory()
        if (story != null) {
            logger.i { "Today's story fetched successfully: ${story.story.title}" }
            Result.success(story)
        } else {
            logger.e { "No story available for today" }
            Result.failure(Exception("No story available"))
        }
    } catch (e: Exception) {
        logger.e(throwable = e) { "Failed to get today's story: ${e.message}" }
        Result.failure(e)
    }
}
