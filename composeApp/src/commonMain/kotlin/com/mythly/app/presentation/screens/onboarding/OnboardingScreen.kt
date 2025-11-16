package com.mythly.app.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Onboarding Screen with pager for multiple onboarding pages
 * Design references: onboarding.html, Onboarding2.html, OnboardingUserJourney.html
 */
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = listOf(
        OnboardingPage.First,
        OnboardingPage.Second,
        OnboardingPage.Third
    )

    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when (pages[currentPage]) {
            is OnboardingPage.First -> FirstOnboardingPage(
                onContinue = { currentPage = 1 },
                onSkip = onSkip,
                currentPage = 0
            )
            is OnboardingPage.Second -> SecondOnboardingPage(
                onContinue = { currentPage = 2 },
                onSkip = onSkip,
                currentPage = 1
            )
            is OnboardingPage.Third -> ThirdOnboardingPage(
                onComplete = onComplete,
                currentPage = 2
            )
        }
    }
}

/**
 * First Onboarding Page - "Ancient Stories, Modern You"
 */
@Composable
private fun FirstOnboardingPage(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    currentPage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Section (50% of screen)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Add actual image from resources
            // For now, placeholder with background color
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF8F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎵",  // Krishna playing flute placeholder
                    fontSize = 120.sp
                )
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Heading
            Text(
                text = "Ancient Stories, Modern You",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = "Discover timeless wisdom from Hindu mythology, one story at a time. Just 5 minutes a day.",
                fontSize = 16.sp,
                color = Color(0xFF808080),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Page Indicators
            PageIndicators(
                pageCount = 3,
                currentPage = currentPage
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF7A072)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip Button
            TextButton(
                onClick = onSkip,
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Skip",
                    fontSize = 16.sp,
                    color = Color(0xFF192A51)
                )
            }
        }
    }
}

/**
 * Second Onboarding Page - "Your Daily Dose of Dharma"
 */
@Composable
private fun SecondOnboardingPage(
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    currentPage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Image Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Add Hanuman carrying mountain image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE8E4DD)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⛰️",  // Mountain placeholder
                    fontSize = 120.sp
                )
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Heading
            Text(
                text = "Your Daily Dose of Dharma",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF181410),
                textAlign = TextAlign.Center,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = "Get a new mythology story delivered every morning. Build wisdom like you build habits.",
                fontSize = 16.sp,
                color = Color(0xFF6C757D),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Page Indicators
            PageIndicators(
                pageCount = 3,
                currentPage = currentPage
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Continue Button
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9933)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Skip Button
            TextButton(
                onClick = onSkip
            ) {
                Text(
                    text = "Skip",
                    fontSize = 14.sp,
                    color = Color(0xFF4169E1)
                )
            }
        }
    }
}

/**
 * Third Onboarding Page - Deity Selection and Preferences
 */
@Composable
private fun ThirdOnboardingPage(
    onComplete: () -> Unit,
    currentPage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F7F5))
    ) {
        // Header Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Color(0xFFE0E7FF))  // Light indigo background
        ) {
            // TODO: Add Ganesha header image
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🐘",  // Ganesha placeholder
                    fontSize = 120.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicators
                PageIndicators(
                    pageCount = 3,
                    currentPage = currentPage
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Heading
                Text(
                    text = "Choose Your Path",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF181410),
                    textAlign = TextAlign.Center,
                    lineHeight = 42.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subheading
                Text(
                    text = "Which deity resonates with you?",
                    fontSize = 16.sp,
                    color = Color(0xFF181410).copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Deity Grid - placeholder, simplified version
                Text(
                    text = "[Deity Selection Grid]",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 32.dp)
                )

                // Time Picker - simplified
                Text(
                    text = "⏰ 7:00 AM",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Start Journey Button
            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9933)
                ),
                shape = CircleShape
            ) {
                Text(
                    text = "Start My Journey",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Page Indicators Component
 */
@Composable
private fun PageIndicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage)
                            Color(0xFFFF9933)  // Active indicator
                        else
                            Color(0xFFFF9933).copy(alpha = 0.3f)  // Inactive indicator
                    )
            )
        }
    }
}

/**
 * Sealed class for onboarding pages
 */
private sealed class OnboardingPage {
    object First : OnboardingPage()
    object Second : OnboardingPage()
    object Third : OnboardingPage()
}
