package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository

class GetTodayStoryUseCase(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke(): Result<StoryUiState> = try {
        val story = storyRepository.getTodayStory()
        if (story != null) {
            Result.success(story)
        } else {
            Result.failure(Exception("No story available"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
