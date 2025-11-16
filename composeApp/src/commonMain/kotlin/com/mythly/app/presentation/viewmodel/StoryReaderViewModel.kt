package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.repository.StoryRepository
import com.mythly.app.domain.usecase.MarkStoryReadUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StoryReaderUiState(
    val isLoading: Boolean = true,
    val story: StoryUiState? = null,
    val error: String? = null,
    val isMarkingAsRead: Boolean = false,
    val readingStartTime: Long? = null
)

class StoryReaderViewModel(
    private val storyRepository: StoryRepository,
    private val markStoryReadUseCase: MarkStoryReadUseCase
) : ViewModel() {

    private val logger = Logger.withTag("StoryReaderViewModel")

    private val _uiState = MutableStateFlow(StoryReaderUiState())
    val uiState: StateFlow<StoryReaderUiState> = _uiState.asStateFlow()

    fun loadStory(storyId: String) {
        viewModelScope.launch {
            logger.d { "Loading story: $storyId" }
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val story = storyRepository.getStoryById(storyId)
                if (story != null) {
                    logger.i { "Story loaded successfully: ${story.story.title}" }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            story = story,
                            readingStartTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                        )
                    }
                } else {
                    logger.e { "Story not found: $storyId" }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Story not found"
                        )
                    }
                }
            } catch (e: Exception) {
                logger.e(throwable = e) { "Failed to load story $storyId: ${e.message}" }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load story"
                    )
                }
            }
        }
    }

    fun markAsRead() {
        viewModelScope.launch {
            val currentStory = _uiState.value.story ?: return@launch
            val startTime = _uiState.value.readingStartTime ?: return@launch

            logger.d { "Marking story as read: ${currentStory.story.id}" }
            _uiState.update { it.copy(isMarkingAsRead = true) }

            val currentTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            val readingTimeSeconds = ((currentTime - startTime) / 1000).toInt()
            logger.d { "Reading time: ${readingTimeSeconds}s" }

            markStoryReadUseCase(
                storyId = currentStory.story.id,
                readingTimeSeconds = readingTimeSeconds,
                usedAudio = false
            )
                .onSuccess {
                    logger.i { "Story marked as read successfully: ${currentStory.story.id}" }
                    // Reload story to update UI with read status
                    loadStory(currentStory.story.id)
                }
                .onFailure { throwable ->
                    logger.e(throwable = throwable) { "Failed to mark story as read: ${throwable.message}" }
                    _uiState.update {
                        it.copy(
                            isMarkingAsRead = false,
                            error = throwable.message ?: "Failed to mark story as read"
                        )
                    }
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val currentStory = _uiState.value.story ?: return@launch

            try {
                val newStatus = !currentStory.isFavorite
                logger.d { "Toggling favorite for ${currentStory.story.id}: $newStatus" }
                storyRepository.markAsFavorite(
                    currentStory.story.id,
                    newStatus
                )
                logger.i { "Favorite status updated successfully: ${currentStory.story.id} -> $newStatus" }
                // Reload story to update UI
                loadStory(currentStory.story.id)
            } catch (e: Exception) {
                logger.e(throwable = e) { "Failed to update favorite status: ${e.message}" }
                _uiState.update {
                    it.copy(error = e.message ?: "Failed to update favorite status")
                }
            }
        }
    }

    fun retry(storyId: String) {
        loadStory(storyId)
    }
}
