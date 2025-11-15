package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _uiState = MutableStateFlow(StoryReaderUiState())
    val uiState: StateFlow<StoryReaderUiState> = _uiState.asStateFlow()

    fun loadStory(storyId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val story = storyRepository.getStoryById(storyId)
                if (story != null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            story = story,
                            readingStartTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Story not found"
                        )
                    }
                }
            } catch (e: Exception) {
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

            _uiState.update { it.copy(isMarkingAsRead = true) }

            val currentTime = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            val readingTimeSeconds = ((currentTime - startTime) / 1000).toInt()

            markStoryReadUseCase(
                storyId = currentStory.story.id,
                readingTimeSeconds = readingTimeSeconds,
                usedAudio = false
            )
                .onSuccess {
                    // Reload story to update UI with read status
                    loadStory(currentStory.story.id)
                }
                .onFailure { throwable ->
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
                storyRepository.markAsFavorite(
                    currentStory.story.id,
                    !currentStory.isFavorite
                )
                // Reload story to update UI
                loadStory(currentStory.story.id)
            } catch (e: Exception) {
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
