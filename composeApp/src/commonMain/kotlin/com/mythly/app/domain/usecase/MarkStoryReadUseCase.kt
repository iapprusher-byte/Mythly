package com.mythly.app.domain.usecase

import co.touchlab.kermit.Logger
import com.mythly.app.domain.model.ReadingSession
import com.mythly.app.domain.repository.StoryRepository
import com.mythly.app.domain.repository.UserRepository
import kotlinx.datetime.Clock

class MarkStoryReadUseCase(
    private val storyRepository: StoryRepository,
    private val userRepository: UserRepository
) {
    private val logger = Logger.withTag("MarkStoryReadUseCase")

    suspend operator fun invoke(
        storyId: String,
        readingTimeSeconds: Int = 0,
        usedAudio: Boolean = false
    ): Result<Unit> = try {
        logger.d { "Marking story as read: $storyId, readingTime: ${readingTimeSeconds}s, audio: $usedAudio" }

        // Mark story as read
        storyRepository.markAsRead(storyId)

        // Update user stats
        userRepository.updateStreak()
        userRepository.incrementStoriesRead()

        if (readingTimeSeconds > 0) {
            val minutes = readingTimeSeconds / 60
            userRepository.updateReadingTime(minutes)
            logger.d { "Updated reading time: $minutes minutes" }
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

        logger.i { "Story marked as read successfully: $storyId" }
        Result.success(Unit)
    } catch (e: Exception) {
        logger.e(throwable = e) { "Failed to mark story as read: $storyId - ${e.message}" }
        Result.failure(e)
    }

    private fun generateId(): String {
        return "${Clock.System.now().toEpochMilliseconds()}-${(0..9999).random()}"
    }
}
