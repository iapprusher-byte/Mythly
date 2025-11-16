package com.mythly.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.Story
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.model.Value
import com.mythly.app.domain.model.toName
import com.mythly.app.presentation.components.EmptyState
import com.mythly.app.presentation.components.ErrorState
import com.mythly.app.presentation.components.LoadingState
import com.mythly.app.presentation.components.StoryCard
import com.mythly.app.presentation.theme.MythlyTheme
import com.mythly.app.presentation.viewmodel.LibraryUiState
import com.mythly.app.presentation.viewmodel.LibraryViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun LibraryScreen(
    onStoryClick: (String) -> Unit,
    viewModel: LibraryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header and Search
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Library",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search stories...") },
                leadingIcon = {
                    Icon(
                        imageVector = FeatherIcons.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true
            )
        }

        when {
            uiState.isLoading -> {
                LoadingState(message = "Loading library...")
            }

            uiState.error != null -> {
                ErrorState(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.retry() }
                )
            }

            uiState.filteredStories.isEmpty() -> {
                EmptyState(
                    message = if (uiState.searchQuery.isNotBlank()) {
                        "No stories found for \"${uiState.searchQuery}\""
                    } else {
                        "No stories available"
                    }
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.filteredStories,
                        key = { it.story.id }
                    ) { story ->
                        StoryCard(
                            storyUiState = story,
                            onClick = { onStoryClick(story.story.id) }
                        )
                    }

                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun LibraryScreenPreview() {
    MythlyTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header and Search
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Library",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Search Bar
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Search stories...") },
                        leadingIcon = {
                            Icon(
                                imageVector = FeatherIcons.Search,
                                contentDescription = "Search"
                            )
                        },
                        singleLine = true
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(3) { index ->
                        StoryCard(
                            storyUiState = StoryUiState(
                                story = Story(
                                    id = "$index",
                                    title = "The Tale of Lord ${listOf("Vishnu", "Shiva", "Rama")[index]}",
                                    content = "A powerful story about ancient wisdom...",
                                    moralLesson = "Wisdom and compassion",
                                    deities = listOf(
                                        listOf(Deity.VISHNU).map { it.toName() },
                                        listOf(Deity.SHIVA).map { it.toName() },
                                        listOf(Deity.RAMA).map { it.toName() }
                                    )[index],
                                    epic = Epic.BHAGAVATA_PURANA.displayName,
                                    readTimeMinutes = 8,
                                    datePublished = Clock.System.now().toEpochMilliseconds(),
                                    values = listOf(Value.WISDOM, Value.COMPASSION).map { it.toName() },
                                    imageUrl = listOf(
                                        "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80",
                                        "https://images.unsplash.com/photo-1571897401841-559e42875ff2?w=800&q=80",
                                        "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=800&q=80"
                                    )[index]
                                ),
                                isRead = index == 0,
                                isFavorite = false
                            ),
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}
