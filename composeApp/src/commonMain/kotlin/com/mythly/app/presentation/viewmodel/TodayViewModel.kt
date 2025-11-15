package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.model.UserStats
import com.mythly.app.domain.usecase.GetTodayStoryUseCase
import com.mythly.app.domain.usecase.GetUserStatsUseCase
import com.mythly.app.domain.usecase.LoadInitialContentUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class TodayUiState(
    val isLoading: Boolean = true,
    val todayStory: StoryUiState? = null,
    val userStats: UserStats = UserStats(),
    val error: String? = null
)

class TodayViewModel(
    private val getTodayStoryUseCase: GetTodayStoryUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val loadInitialContentUseCase: LoadInitialContentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadTodayContent()
    }

    fun loadTodayContent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load initial content from JSON if needed
            loadInitialContentUseCase()
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to load stories"
                        )
                    }
                    return@launch
                }

            // Collect user stats
            getUserStatsUseCase()
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            error = throwable.message ?: "Failed to load user stats"
                        )
                    }
                }
                .collectLatest { stats ->
                    _uiState.update { it.copy(userStats = stats) }
                }

            // Get today's story
            getTodayStoryUseCase()
                .onSuccess { story ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todayStory = story,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to load today's story"
                        )
                    }
                }
        }
    }

    fun retry() {
        loadTodayContent()
    }
}
