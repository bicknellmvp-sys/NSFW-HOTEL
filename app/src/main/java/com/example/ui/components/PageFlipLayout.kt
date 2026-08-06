package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ColoringPage
import kotlinx.coroutines.launch

/**
 * 3D Animated Flip Page Layout with realistic physical book appearance
 */
@Composable
fun PageFlipLayout(
    page: ColoringPage,
    totalPages: Int,
    isFavorite: Boolean,
    progressPercentage: Int,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onOpenStudio: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    pageContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val flipRotation = remember { Animatable(0f) }

    val shadowGradient = Brush.horizontalGradient(
        colors = listOf(
            Color.Black.copy(alpha = 0.25f),
            Color.Black.copy(alpha = 0.05f),
            Color.Transparent,
            Color.Black.copy(alpha = 0.05f),
            Color.Black.copy(alpha = 0.20f)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Book Container Card with 3D Page Curl effect
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(20.dp))
                .pointerInput(page.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (flipRotation.value > 30f) {
                                scope.launch {
                                    flipRotation.animateTo(90f, spring())
                                    onNextPage()
                                    flipRotation.snapTo(0f)
                                }
                            } else if (flipRotation.value < -30f) {
                                scope.launch {
                                    flipRotation.animateTo(-90f, spring())
                                    onPreviousPage()
                                    flipRotation.snapTo(0f)
                                }
                            } else {
                                scope.launch { flipRotation.animateTo(0f, spring()) }
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val newRot = (flipRotation.value - dragAmount * 0.25f).coerceIn(-85f, 85f)
                            scope.launch { flipRotation.snapTo(newRot) }
                        }
                    )
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF9)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(2.dp, Color(0xFFEFE8D8), RoundedCornerShape(20.dp))
            ) {
                // Background paper texture & center spine binder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(shadowGradient)
                )

                // Left Binder Rings (Flipbook effect)
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(8) {
                        Box(
                            modifier = Modifier
                                .size(width = 16.dp, height = 24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF8D8D8D), Color(0xFFE0E0E0), Color(0xFF616161))
                                    )
                                )
                                .border(1.dp, Color(0xFF424242), RoundedCornerShape(8.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .align(Alignment.Center)
                                    .background(Color(0xFF333333), CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }
                }

                // Page Header Bar inside Book
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 20.dp, top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Page ${page.pageNumber} / $totalPages",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = page.difficulty,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Text(
                            text = page.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2C221E)
                        )
                    }

                    // Favorite Ribbon
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.testTag("bookmark_button")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark Page",
                            tint = if (isFavorite) Color(0xFFFF5252) else Color.Gray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                // Main Page Art Container with 3D Flip Layer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 36.dp, end = 20.dp, top = 68.dp, bottom = 64.dp)
                        .graphicsLayer {
                            rotationY = flipRotation.value
                            cameraDistance = 12 * density
                        }
                ) {
                    pageContent()
                }

                // Bottom Page Footer inside Book
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 36.dp, end = 20.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )

                    // Open Studio Button inside page corner
                    Surface(
                        onClick = onOpenStudio,
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp,
                        modifier = Modifier.testTag("start_coloring_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Color Page",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Color Studio",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Control Row under Book
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousPage,
                enabled = page.pageNumber > 1,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (page.pageNumber > 1) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f),
                        CircleShape
                    )
                    .testTag("prev_page_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Page",
                    tint = if (page.pageNumber > 1) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                )
            }

            Text(
                text = "Drag page corner or swipe left/right to flip",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            IconButton(
                onClick = onNextPage,
                enabled = page.pageNumber < totalPages,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (page.pageNumber < totalPages) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f),
                        CircleShape
                    )
                    .testTag("next_page_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Page",
                    tint = if (page.pageNumber < totalPages) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                )
            }
        }
    }
}
