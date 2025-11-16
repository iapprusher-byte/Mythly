package com.mythly.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mythly.app.presentation.theme.MythlyTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertCircle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
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
                imageVector = FeatherIcons.AlertCircle,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onRetry) {
                    Text("Try Again")
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorStatePreview() {
    MythlyTheme {
        Surface {
            ErrorState(
                message = "Failed to load stories. Please check your connection.",
                onRetry = {}
            )
        }
    }
}

@Preview
@Composable
fun ErrorStateNoRetryPreview() {
    MythlyTheme(darkTheme = true) {
        Surface {
            ErrorState(
                message = "Unable to connect to the server",
                onRetry = null
            )
        }
    }
}
