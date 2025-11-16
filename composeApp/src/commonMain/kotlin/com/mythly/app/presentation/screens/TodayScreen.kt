package com.mythly.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mythly.app.domain.model.*
import com.mythly.app.presentation.components.ErrorState
import com.mythly.app.presentation.components.LoadingState
import com.mythly.app.presentation.components.StoryCard
import com.mythly.app.presentation.components.StreakWidget
import com.mythly.app.presentation.theme.MythlyTheme
import com.mythly.app.presentation.viewmodel.TodayViewModel
import com.mythly.app.presentation.viewmodel.TodayUiState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodayScreen(
    onStoryClick: (String) -> Unit,
    viewModel: TodayViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            LoadingState(message = "Loading today's story...")
        }

        uiState.error != null -> {
            ErrorState(
                message = uiState.error ?: "Unknown error",
                onRetry = { viewModel.retry() }
            )
        }

        else -> {
            TodayContent(
                uiState = uiState,
                onStoryClick = onStoryClick
            )
        }
    }
}

@Composable
private fun TodayContent(
    uiState: com.mythly.app.presentation.viewmodel.TodayUiState,
    onStoryClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Today's Story",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start your day with ancient wisdom",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Streak Widget
        Box(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            StreakWidget(userStats = uiState.userStats)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Today's Story Card
        uiState.todayStory?.let { story ->
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Featured Story",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                StoryCard(
                    story = story,
                    onClick = { onStoryClick(story.story.id) }
                )
            }
        }

        // Bottom spacing
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
fun TodayContentPreview() {
    MythlyTheme {
        Surface {
            TodayContent(
                uiState = TodayUiState(
                    isLoading = false,
                    todayStory = StoryUiState(
                        story = Story(
                            id = "1",
                            title = "The Legend of Lord Rama",
                            content = "The epic tale of Lord Rama, the seventh avatar of Vishnu...",
                            moralLesson = "Righteousness and dharma always triumph",
                            deities = listOf(Deity.RAMA),
                            epic = Epic.RAMAYANA,
                            readTimeMinutes = 12,
                            datePublished = System.currentTimeMillis(),
                            values = listOf(
                                Value.DHARMA,
                                Value.COURAGE
                            ),
                            imageUrl = "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=800&q=80"
                        ),
                        isRead = false,
                        isFavorite = false
                    ),
                    userStats = UserStats(
                        currentStreak = 5,
                        longestStreak = 12,
                        totalStoriesRead = 28,
                        totalReadingTimeMinutes = 224,
                        readingStreak7Day = false,
                        readingStreak30Day = false,
                        readingStreak100Day = false
                    )
                ),
                onStoryClick = {}
            )
        }
    }
}
