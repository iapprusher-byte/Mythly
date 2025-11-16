package com.mythly.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mythly.app.domain.model.UserStats
import com.mythly.app.presentation.theme.MythlyTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Zap
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StreakWidget(
    userStats: UserStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Streak flame icon and count
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = FeatherIcons.Zap,
                        contentDescription = "Streak",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Column {
                    Text(
                        text = "${userStats.currentStreak}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Day Streak",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            // Longest streak
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Best: ${userStats.longestStreak}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${userStats.totalStoriesRead} stories read",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview
@Composable
fun StreakWidgetPreview() {
    MythlyTheme {
        Surface {
            StreakWidget(
                userStats = UserStats(
                    currentStreak = 7,
                    longestStreak = 15,
                    totalStoriesRead = 42,
                    totalReadingTimeMinutes = 336,
                    readingStreak7Day = true,
                    readingStreak30Day = false,
                    readingStreak100Day = false
                )
            )
        }
    }
}

@Preview
@Composable
fun StreakWidgetLongStreakPreview() {
    MythlyTheme(darkTheme = true) {
        Surface {
            StreakWidget(
                userStats = UserStats(
                    currentStreak = 105,
                    longestStreak = 150,
                    totalStoriesRead = 215,
                    totalReadingTimeMinutes = 1720,
                    readingStreak7Day = true,
                    readingStreak30Day = true,
                    readingStreak100Day = true
                )
            )
        }
    }
}
