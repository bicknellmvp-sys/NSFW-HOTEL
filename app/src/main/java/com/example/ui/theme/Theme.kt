package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NsfwColorScheme =
    darkColorScheme(
        primary = NsfwPrimary,
        secondary = NsfwSecondary,
        secondaryContainer = NsfwSecondaryContainer,
        background = NsfwBackground,
        surface = NsfwSurface,
        surfaceContainer = NsfwSurfaceContainer,
        surfaceContainerHigh = NsfwSurfaceContainerHigh,
        surfaceContainerHighest = NsfwSurfaceContainerHighest,
        onBackground = NsfwOnBackground,
        onSurface = NsfwOnSurface,
        onSurfaceVariant = NsfwOnSurfaceVariant,
        outline = NsfwOutline,
        outlineVariant = NsfwOutlineVariant
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = NsfwColorScheme,
        typography = Typography,
        content = content
    )
}

