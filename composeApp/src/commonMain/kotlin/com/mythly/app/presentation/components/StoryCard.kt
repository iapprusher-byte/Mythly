package com.mythly.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mythly.app.domain.model.StoryUiState
import com.mythly.app.domain.model.Story
import com.mythly.app.domain.model.Deity
import com.mythly.app.domain.model.Epic
import com.mythly.app.domain.model.Value
import com.mythly.app.presentation.theme.MythlyTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.CheckCircle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StoryCard(
    story: StoryUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showReadStatus: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            // Story Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = story.story.imageUrl,
                    contentDescription = story.story.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Read indicator overlay
                if (showReadStatus && story.isRead) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = FeatherIcons.CheckCircle,
                                contentDescription = "Read",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Read",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Story Details
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Title
                Text(
                    text = story.story.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Sanskrit title if available
                story.story.sanskritTitle?.let { sanskritTitle ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sanskritTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Metadata Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Deity badge
                    SuggestionChip(
                        onClick = { },
                        label = {
                            Text(
                                text = story.story.deity.name,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )

                    // Read time
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = FeatherIcons.Clock,
                            contentDescription = "Read time",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${story.story.readTimeMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun StoryCardPreview() {
    MythlyTheme {
        StoryCard(
            story = StoryUiState(
                story = Story(
                    id = "1",
                    title = "The Tale of Lord Vishnu",
                    sanskritTitle = "विष्णु कथा",
                    content = "A powerful story about the preserver of the universe...",
                    moralLesson = "Preserve and protect the good in the world",
                    deity = Deity(id = "1", name = "Vishnu", description = "The Preserver"),
                    epic = Epic(id = "1", name = "Bhagavata Purana"),
                    readTimeMinutes = 8,
                    values = listOf(
                        Value(id = "1", name = "Protection", description = ""),
                        Value(id = "2", name = "Duty", description = "")
                    ),
                    imageUrl = "https://placehold.co/600x400/004E89/FFFFFF/png?text=Lord+Vishnu",
                    difficulty = "Beginner"
                ),
                isRead = false,
                isFavorite = false
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun StoryCardReadPreview() {
    MythlyTheme {
        StoryCard(
            story = StoryUiState(
                story = Story(
                    id = "2",
                    title = "Krishna and the Butter",
                    sanskritTitle = "कृष्ण माखन चोर",
                    content = "The mischievous adventures of young Krishna...",
                    moralLesson = "Joy and innocence are divine qualities",
                    deity = Deity(id = "2", name = "Krishna", description = "The Divine Child"),
                    epic = Epic(id = "2", name = "Bhagavata Purana"),
                    readTimeMinutes = 5,
                    values = listOf(
                        Value(id = "3", name = "Joy", description = ""),
                        Value(id = "4", name = "Love", description = "")
                    ),
                    imageUrl = "https://placehold.co/600x400/FF6B35/FFFFFF/png?text=Krishna",
                    difficulty = "Beginner"
                ),
                isRead = true,
                isFavorite = true
            ),
            onClick = {}
        )
    }
}
