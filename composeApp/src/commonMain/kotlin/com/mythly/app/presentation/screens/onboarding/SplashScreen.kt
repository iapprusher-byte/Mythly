package com.mythly.app.presentation.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Splash Screen - Initial loading screen with app branding
 * Design reference: Splash.html
 */
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onSplashComplete: () -> Unit = {}
) {
    // Gradient background from cream to white
    val gradientColors = listOf(
        Color(0xFFFFF8F0),  // #FFF8F0
        Color.White
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
                tint = Color(0xFFFF9933)  // Primary color
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "Mythly",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                letterSpacing = (-0.3).sp,
                // Use serif font family - will be styled with Playfair Display
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tagline
            Text(
                text = "Ancient wisdom, daily",
                fontSize = 16.sp,
                color = Color(0xFF757575),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // Loading Spinner
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Color(0xFFFF9933),
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
    tint: Color = Color(0xFFFF9933)
) {
    // Placeholder - using a simple circular icon
    // In production, convert the SVG lotus from HTML to vector drawable
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🪷",  // Lotus emoji as placeholder
            fontSize = 80.sp
        )
    }
}

/**
 * Preview for Splash Screen (Light theme)
 */
//@Preview(
//    name = "Splash Screen - Light",
//    showBackground = true,
//    backgroundColor = 0xFFFFFFFF
//)
//@Composable
//private fun SplashScreenPreview() {
//    MythlyTheme {
//        SplashScreen()
//    }
//}
