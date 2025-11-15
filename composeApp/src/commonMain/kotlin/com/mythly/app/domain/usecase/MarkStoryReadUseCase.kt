package com.mythly.app.domain.usecase

import com.mythly.app.domain.model.ReadingSession
import com.mythly.app.domain.repository.StoryRepository
import com.mythly.app.domain.repository.UserRepository
import kotlinx.datetime.Clock

class MarkStoryReadUseCase(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        storyId: String,
        readingTimeSeconds: Int = 0,
        usedAudio: Boolean = false
    ): Result<Unit> = try {
        // Mark story as read
        storyRepository.markAsRead(storyId)

        // Update user stats
        userRepository.updateStreak()
        userRepository.incrementStoriesRead()

        if (readingTimeSeconds > 0) {
            userRepository.updateReadingTime(readingTimeSeconds / 60)
        }

        // Save reading session
        val session = ReadingSession(
            id = generateId(),
            storyId = storyId,
            startedAt = Clock.System.now().toEpochMilliseconds(),
            completedAt = Clock.System.now().toEpochMilliseconds(),
            readingTimeSeconds = readingTimeSeconds,
            usedAudio = usedAudio
        )
        userRepository.saveReadingSession(session)

        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun generateId(): String {
        return "${Clock.System.now().toEpochMilliseconds()}-${(0..9999).random()}"
    }
}
