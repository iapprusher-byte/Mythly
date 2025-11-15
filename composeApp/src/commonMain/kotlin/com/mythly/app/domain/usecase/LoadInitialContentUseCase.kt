package com.mythly.app.domain.usecase

import com.mythly.app.domain.repository.StoryRepository

class LoadInitialContentUseCase(
    private val storyRepository: StoryRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return storyRepository.loadInitialContent()
    }
}
