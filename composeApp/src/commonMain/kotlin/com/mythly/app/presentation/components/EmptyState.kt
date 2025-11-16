package com.mythly.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mythly.app.presentation.theme.MythlyTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Search
import compose.icons.feathericons.BookOpen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptyState(
    message: String,
    icon: ImageVector = FeatherIcons.Search,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Empty",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun EmptyStatePreview() {
    MythlyTheme {
        Surface {
            EmptyState(
                message = "No stories found matching your search",
                icon = FeatherIcons.Search
            )
        }
    }
}

@Preview
@Composable
fun EmptyStateNoStoriesPreview() {
    MythlyTheme(darkTheme = true) {
        Surface {
            EmptyState(
                message = "You haven't read any stories yet",
                icon = FeatherIcons.BookOpen
            )
        }
    }
}
