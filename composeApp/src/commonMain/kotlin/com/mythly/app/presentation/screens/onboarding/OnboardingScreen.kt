package com.mythly.app.presentation.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mythly.app.presentation.components.MythlyButton
import com.mythly.app.presentation.components.MythlyTextButton
import com.mythly.app.presentation.theme.MythlyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (pages[currentPage]) {
            is OnboardingPage.First -> FirstOnboardingPage(
                imageUrl = "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?w=800&q=80&auto=format&fit=crop",
                title = "Ancient Stories, Modern You",
                description = "Discover timeless wisdom from Hindu mythology, one story at a time. Just 5 minutes a day.",
                onContinue = { currentPage = 1 },
                onSkip = onSkip,
                currentPage = 0
            )

            is OnboardingPage.Second -> FirstOnboardingPage(
                imageUrl = "https://images.unsplash.com/photo-1707833684948-11bd776ffdef?q=80&w=927&auto=format&fit=crop",
                title = "Your Daily Dose of Dharma",
                description = "Get a new mythology story delivered every morning. Build wisdom like you build habits.",
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
    imageUrl: String,
    title: String,
    description: String,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
    currentPage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Section (fixed 300dp height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Krishna",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Heading
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Page Indicators
                PageIndicators(
                    pageCount = 3,
                    currentPage = currentPage
                )
            }

            Spacer(Modifier.weight(1f))

            // Bottom Buttons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Continue Button
                MythlyButton(
                    text = "Continue",
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Skip Button
                MythlyTextButton(
                    text = "Skip",
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth(0.7f)
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
    val deities = remember {
        listOf(
            DeityInfo(
                "Ganesha",
                "🐘",
                "https://images.unsplash.com/photo-1587582423116-ec07293f0395?w=400&q=80&auto=format&fit=crop"
            ),
            DeityInfo(
                "Krishna",
                "🪈",
                "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?w=400&q=80&auto=format&fit=crop"
            ),
            DeityInfo(
                "Shiva",
                "🔱",
                "https://images.unsplash.com/photo-1571897401841-559e42875ff2?w=400&q=80&auto=format&fit=crop"
            ),
            DeityInfo(
                "Rama",
                "🏹",
                "https://images.unsplash.com/photo-1604608672516-f1b0e1f04b6a?w=400&q=80&auto=format&fit=crop"
            ),
            DeityInfo(
                "Hanuman",
                "⛰️",
                "https://images.unsplash.com/photo-1707833684948-11bd776ffdef?q=80&w=400&auto=format&fit=crop"
            ),
            DeityInfo(
                "Durga",
                "⚔️",
                "https://images.unsplash.com/photo-1609619385002-8d03e5bb8415?w=400&q=80&auto=format&fit=crop"
            )
        )
    }

    var selectedDeity by remember { mutableStateOf<DeityInfo?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header Image (fixed 300dp height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1587582423116-ec07293f0395?w=800&q=80&auto=format&fit=crop",
                contentDescription = "Ganesha",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Heading
                Text(
                    text = "Choose Your Path",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subheading
                Text(
                    text = "Which deity resonates with you?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Page Indicators
                PageIndicators(
                    pageCount = 3,
                    currentPage = currentPage
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Deity Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(deities) { deity ->
                        DeityCard(
                            deity = deity,
                            isSelected = selectedDeity == deity,
                            onClick = { selectedDeity = deity }
                        )
                    }
                }
            }

            // Start Journey Button
            MythlyButton(
                text = "Start My Journey",
                onClick = onComplete,
                modifier = Modifier.fillMaxWidth()
            )
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
                            MaterialTheme.colorScheme.primary  // Active indicator
                        else
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)  // Inactive indicator
                    )
            )
        }
    }
}

/**
 * Deity Card Component for selection
 */
@Composable
private fun DeityCard(
    deity: DeityInfo,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Deity Image
            AsyncImage(
                model = deity.imageUrl,
                contentDescription = deity.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Deity Name
            Text(
                text = deity.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

/**
 * Data class for deity information
 */
private data class DeityInfo(
    val name: String,
    val emoji: String,
    val imageUrl: String
)

/**
 * Sealed class for onboarding pages
 */
private sealed class OnboardingPage {
    object First : OnboardingPage()
    object Second : OnboardingPage()
    object Third : OnboardingPage()
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    MythlyTheme {
        Surface {
            OnboardingScreen(
                onComplete = {},
                onSkip = {}
            )
        }
    }
}

@Preview
@Composable
fun DeityCardPreview() {
    MythlyTheme {
        Surface {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DeityCard(
                    deity = DeityInfo(
                        "Ganesha",
                        "🐘",
                        "https://images.unsplash.com/photo-1587582423116-ec07293f0395?w=400&q=80&auto=format&fit=crop"
                    ),
                    isSelected = true,
                    onClick = {},
                    modifier = Modifier.width(100.dp)
                )
                DeityCard(
                    deity = DeityInfo(
                        "Krishna",
                        "🪈",
                        "https://images.unsplash.com/photo-1582510003544-4d00b7f74220?w=400&q=80&auto=format&fit=crop"
                    ),
                    isSelected = false,
                    onClick = {},
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}
