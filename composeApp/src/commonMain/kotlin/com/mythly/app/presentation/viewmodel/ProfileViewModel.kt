package com.mythly.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // Collect user stats
                getUserStatsUseCase()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.message ?: "Failed to load user stats"
                            )
                        }
                    }
                    .collectLatest { stats ->
                        _uiState.update {
                            it.copy(
                                userStats = stats,
                                isLoading = false
                            )
                        }
                    }

                // Collect recent reading sessions
                userRepository.getRecentSessions()
                    .catch { throwable ->
                        _uiState.update {
                            it.copy(error = throwable.message ?: "Failed to load reading sessions")
                        }
                    }
                    .collectLatest { sessions ->
                        _uiState.update { it.copy(recentSessions = sessions) }
                    }
            } catch (e: Exception) {
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
