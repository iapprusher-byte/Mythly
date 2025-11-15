package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
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

    private val logger = Logger.withTag("TodayViewModel")

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadTodayContent()
    }

    fun loadTodayContent() {
        viewModelScope.launch {
            logger.d { "Loading today's content..." }
            _uiState.update { it.copy(isLoading = true, error = null) }

            // Load initial content from JSON if needed
            loadInitialContentUseCase()
                .onFailure { throwable ->
                    logger.e(throwable = throwable) { "Failed to load initial content: ${throwable.message}" }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Failed to load stories"
                        )
                    }
                    return@launch
                }

            logger.d { "Loading user stats..." }
            // Collect user stats
            getUserStatsUseCase()
                .catch { throwable ->
                    logger.e(throwable = throwable) { "Failed to load user stats: ${throwable.message}" }
                    _uiState.update {
                        it.copy(
                            error = throwable.message ?: "Failed to load user stats"
                        )
                    }
                }
                .collectLatest { stats ->
                    logger.d { "User stats loaded: streak=${stats.currentStreak}, storiesRead=${stats.totalStoriesRead}" }
                    _uiState.update { it.copy(userStats = stats) }
                }

            logger.d { "Loading today's story..." }
            // Get today's story
            getTodayStoryUseCase()
                .onSuccess { story ->
                    logger.i { "Today's story loaded successfully: ${story?.story?.title}" }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            todayStory = story,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    logger.e(throwable = throwable) { "Failed to load today's story: ${throwable.message}" }
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
