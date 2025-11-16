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
import com.mythly.app.presentation.components.EmptyState
import com.mythly.app.presentation.components.ErrorState
import com.mythly.app.presentation.components.LoadingState
import com.mythly.app.presentation.components.StoryCard
import com.mythly.app.presentation.viewmodel.LibraryViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import org.koin.compose.viewmodel.koinViewModel

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
