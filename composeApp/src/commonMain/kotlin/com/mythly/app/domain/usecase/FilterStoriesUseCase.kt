package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class FilterStoriesByDeityUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(deity: Deity): Flow<List<StoryUiState>> {
        return storyRepository.getStoriesByDeity(deity)
    }
}

class FilterStoriesByEpicUseCase(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(epic: Epic): Flow<List<StoryUiState>> {
        return storyRepository.getStoriesByEpic(epic)
    }
}
