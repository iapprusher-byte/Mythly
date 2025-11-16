package com.mythly.app.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mythly.app.presentation.theme.MythlyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LoadingState(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
fun LoadingStatePreview() {
    MythlyTheme {
        Surface {
            LoadingState(message = "Loading stories...")
        }
    }
}

@Preview
@Composable
fun LoadingStateDarkPreview() {
    MythlyTheme(darkTheme = true) {
        Surface {
            LoadingState(message = "Please wait...")
        }
    }
}
