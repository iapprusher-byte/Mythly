package com.mythly.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = White,
    primaryContainer = SaffronLight,
    onPrimaryContainer = Black,

    secondary = SkyBlue,
    onSecondary = White,
    secondaryContainer = SkyBlueLight,
    onSecondaryContainer = Black,

    tertiary = GoldenYellow,
    onTertiary = Black,

    background = BackgroundLight,
    onBackground = Black,

    surface = SurfaceLight,
    onSurface = Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = DarkGray,

    error = Error,
    onError = White,

    outline = MediumGray,
    outlineVariant = LightGray
)

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    onPrimary = Black,
    primaryContainer = SaffronDark,
    onPrimaryContainer = White,

    secondary = SkyBlue,
    onSecondary = Black,
    secondaryContainer = SkyBlueDark,
    onSecondaryContainer = White,

    tertiary = GoldenYellow,
    onTertiary = Black,

    background = BackgroundDark,
    onBackground = White,

    surface = SurfaceDark,
    onSurface = White,
    surfaceVariant = CardDark,
    onSurfaceVariant = LightGray,

    error = Error,
    onError = Black,

    outline = MediumGray,
    outlineVariant = DarkGray
)

@Composable
fun MythlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MythlyTypography,
        content = content
    )
}
