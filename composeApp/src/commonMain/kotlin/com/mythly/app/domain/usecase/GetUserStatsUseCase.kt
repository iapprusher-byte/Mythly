package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.UserStats
import com.mythly.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserStatsUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserStats> {
        return userRepository.getUserStats()
    }
}
