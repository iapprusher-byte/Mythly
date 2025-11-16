package com.mythly.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.Story
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.model.Value
import com.mythly.app.presentation.components.ErrorState
import com.mythly.app.presentation.components.LoadingState
import com.mythly.app.presentation.theme.MythlyTheme
import com.mythly.app.presentation.viewmodel.StoryReaderUiState
import com.mythly.app.presentation.viewmodel.StoryReaderViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Heart
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryReaderScreen(
    storyId: String,
    onNavigateBack: () -> Unit,
    viewModel: StoryReaderViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Story") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = FeatherIcons.ArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    uiState.story?.let { story ->
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = FeatherIcons.Heart,
                                contentDescription = "Favorite",
                                tint = if (story.isFavorite) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingState(message = "Loading story...")
            }

            uiState.error != null -> {
                ErrorState(
                    message = uiState.error ?: "Unknown error",
                    onRetry = { viewModel.retry(storyId) }
                )
            }

            uiState.story != null -> {
                StoryContent(
                    uiState = uiState,
                    onMarkAsRead = { viewModel.markAsRead() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun StoryContent(
    uiState: com.mythly.app.presentation.viewmodel.StoryReaderUiState,
    onMarkAsRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    val story = uiState.story ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Story Image
        AsyncImage(
            model = story.story.imageUrl,
            contentDescription = story.story.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Title
            Text(
                text = story.story.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Metadata chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Display deity chips (all deities)
                story.story.deities.forEach { deity ->
                    AssistChip(
                        onClick = { },
                        label = { Text(deity.displayName) }
                    )
                }
                AssistChip(
                    onClick = { },
                    label = { Text(story.story.epic.displayName) }
                )
                AssistChip(
                    onClick = { },
                    label = { Text("${story.story.readTimeMinutes} min read") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))

            // Content
            Text(
                text = story.story.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight.times(1.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))

            // Moral Lesson Section
            Text(
                text = "Moral Lesson",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = story.story.moralLesson,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Values Section
            Text(
                text = "Values Illustrated",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                story.story.values.forEach { value ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(value.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Mark as Read Button
            if (!story.isRead) {
                Button(
                    onClick = onMarkAsRead,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isMarkingAsRead
                ) {
                    if (uiState.isMarkingAsRead) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (uiState.isMarkingAsRead) "Marking as Read..." else "Mark as Read")
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = FeatherIcons.Heart,
                        contentDescription = "Completed",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Story Completed",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Bottom spacing
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun StoryContentPreview() {
    MythlyTheme {
        Surface {
            StoryContent(
                uiState = StoryReaderUiState(
                    isLoading = false,
                    story = StoryUiState(
                        story = Story(
                            id = "1",
                            title = "The Legend of Lord Rama",
                            content = """
                                In the ancient kingdom of Ayodhya, there lived a prince named Rama, the eldest son of King Dasharatha.
                                Known for his righteousness, compassion, and unwavering adherence to dharma, Rama was beloved by all.

                                When the time came for Rama to be crowned king, his stepmother Kaikeyi, influenced by her maid,
                                demanded that her son Bharata be made king instead, and that Rama be exiled to the forest for fourteen years.

                                Though heartbroken, King Dasharatha had to honor the boons he had promised Kaikeyi long ago.
                                Rama, ever dutiful, accepted the exile without hesitation, choosing to uphold his father's word over his own desires.

                                Accompanied by his devoted wife Sita and loyal brother Lakshmana, Rama ventured into the forest,
                                where he would face great trials and demonstrate the true meaning of dharma, loyalty, and sacrifice.
                            """.trimIndent(),
                            moralLesson = "True nobility lies not in power or position, but in unwavering adherence to dharma and duty, even when it requires great personal sacrifice.",
                            deities = listOf(Deity.RAMA),
                            epic = Epic.RAMAYANA,
                            readTimeMinutes = 12,
                            datePublished = Clock.System.now().toEpochMilliseconds(),
                            values = listOf(
                                Value.DHARMA,
                                Value.COURAGE,
                                Value.DUTY
                            ),
                            imageUrl = "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=800&q=80"
                        ),
                        isRead = false,
                        isFavorite = false
                    ),
                    error = null,
                    isMarkingAsRead = false
                ),
                onMarkAsRead = {}
            )
        }
    }
}
