package com.mythly.app.presentation.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mythly.app.presentation.theme.MythlyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Splash Screen - Initial loading screen with app branding
 * Design reference: Splash.html
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashComplete: () -> Unit = {}
) {
    // Gradient background from primaryContainer to background
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.background
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(colors = gradientColors)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            // Lotus Icon (simplified version using text)
            // In production, use actual SVG/vector asset
            LotusIcon(
                modifier = Modifier.size(112.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Mythly",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Ancient wisdom, daily",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Loading Spinner
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

/**
 * Simplified Lotus Icon using Compose shapes
 * TODO: Replace with actual SVG vector drawable
 */
@Composable
private fun LotusIcon(
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color(0xFFFF9933)
) {
    // Placeholder - using a simple circular icon
    // In production, convert the SVG lotus from HTML to vector drawable
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🪷",  // Lotus emoji as placeholder
            style = MaterialTheme.typography.displayLarge
        )
    }
}

/**
 * Preview for Splash Screen (Light theme)
 */
@Preview
@Composable
private fun SplashScreenPreview() {
    MythlyTheme {
        Surface {
            SplashScreen()
        }
    }
}
