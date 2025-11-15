package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllStoriesUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(): Flow<List<StoryUiState>> {
        return storyRepository.getAllStories()
    }
}
