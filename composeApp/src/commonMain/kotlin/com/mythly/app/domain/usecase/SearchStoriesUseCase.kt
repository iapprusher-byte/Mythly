package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class SearchStoriesUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(query: String): Flow<List<StoryUiState>> {
        return storyRepository.searchStories(query)
    }
}
