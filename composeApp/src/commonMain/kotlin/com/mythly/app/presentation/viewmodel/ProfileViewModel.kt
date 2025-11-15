package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mythly.app.domain.model.ReadingSession
import com.mythly.app.domain.model.UserStats
import com.mythly.app.domain.repository.UserRepository
import com.mythly.app.domain.usecase.GetUserStatsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val userStats: UserStats = UserStats(),
    val recentSessions: List<ReadingSession> = emptyList(),
    val error: String? = null
)

class ProfileViewModel(
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val logger = Logger.withTag("ProfileViewModel")

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            logger.d { "Loading profile data..." }
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Collect user stats
                logger.d { "Loading user stats..." }
                getUserStatsUseCase()
                    .catch { throwable ->
                        logger.e(throwable = throwable) { "Failed to load user stats: ${throwable.message}" }
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message ?: "Failed to load user stats"
                            )
                        }
                    }
                    .collectLatest { stats ->
                        logger.i { "User stats loaded: streak=${stats.currentStreak}, storiesRead=${stats.totalStoriesRead}" }
                        _uiState.update {
                            it.copy(
                                userStats = stats,
                                isLoading = false
                            )
                        }
                    }

                // Collect recent reading sessions
                logger.d { "Loading recent reading sessions..." }
                userRepository.getRecentSessions()
                    .catch { throwable ->
                        logger.e(throwable = throwable) { "Failed to load reading sessions: ${throwable.message}" }
                        _uiState.update {
                            it.copy(error = throwable.message ?: "Failed to load reading sessions")
                        }
                    }
                    .collectLatest { sessions ->
                        logger.i { "Loaded ${sessions.size} recent reading sessions" }
                        _uiState.update { it.copy(recentSessions = sessions) }
                    }
            } catch (e: Exception) {
                logger.e(throwable = e) { "Failed to load profile data: ${e.message}" }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load profile data"
                    )
                }
            }
        }
    }

    fun retry() {
        loadProfileData()
    }
}
