package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.PageTemplates
import com.example.ui.components.ColoringCanvas
import com.example.ui.viewmodel.ColoringViewModel

@Composable
fun GalleryScreen(
    viewModel: ColoringViewModel,
    onOpenPageInBook: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedPages by viewModel.savedPages.collectAsState()

    var selectedCategoryFilter by remember { mutableStateOf("all") }
    var selectedTab by remember { mutableStateOf("ALL") } // ALL, FAVORITES, COMPLETED

    val filteredPages = PageTemplates.PAGES.filter { page ->
        val entity = savedPages.find { it.pageId == page.id }
        val categoryMatch = selectedCategoryFilter == "all" || page.categoryId == selectedCategoryFilter
        val tabMatch = when (selectedTab) {
            "FAVORITES" -> entity?.isFavorite == true
            "COMPLETED" -> entity?.isCompleted == true
            else -> true
        }
        categoryMatch && tabMatch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Title Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "COLOR ME NSFW",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Forbidden Sophistication • ${filteredPages.size} Archives",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // View Mode Filter Tabs (All, Favorites, Completed)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedTab == "ALL",
                onClick = { selectedTab = "ALL" },
                label = { Text("All Pages") },
                modifier = Modifier.testTag("gallery_filter_all")
            )
            FilterChip(
                selected = selectedTab == "FAVORITES",
                onClick = { selectedTab = "FAVORITES" },
                label = { Text("Favorites ❤️") },
                modifier = Modifier.testTag("gallery_filter_favorites")
            )
            FilterChip(
                selected = selectedTab == "COMPLETED",
                onClick = { selectedTab = "COMPLETED" },
                label = { Text("Completed ✨") },
                modifier = Modifier.testTag("gallery_filter_completed")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedCategoryFilter == "all",
                    onClick = { selectedCategoryFilter = "all" },
                    label = { Text("All Categories") }
                )
            }
            items(PageTemplates.CATEGORIES) { cat ->
                FilterChip(
                    selected = selectedCategoryFilter == cat.id,
                    onClick = { selectedCategoryFilter = cat.id },
                    label = { Text("${cat.iconEmoji} ${cat.name}") }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid of Page Cards
        if (filteredPages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Empty",
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No pages found in this view",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredPages) { page ->
                    val entity = savedPages.find { it.pageId == page.id }
                    val isFav = entity?.isFavorite ?: false
                    val progress = entity?.progressPercentage ?: 0
                    val isCompleted = entity?.isCompleted ?: false

                    val strokes = viewModel.getStrokesForPage(page.id)
                    val fills = viewModel.getFillsForPage(page.id)
                    val stickers = viewModel.getStickersForPage(page.id)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clickable { onOpenPageInBook(page.pageNumber - 1) }
                            .testTag("gallery_card_${page.id}"),
                        shape = RoundedCornerShape(0.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Mini Canvas Thumbnail Preview
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                ColoringCanvas(
                                    pageId = page.id,
                                    selectedTool = viewModel.selectedTool.value,
                                    selectedColor = viewModel.selectedColor.value,
                                    brushSize = viewModel.brushSize.value,
                                    brushAlpha = viewModel.brushAlpha.value,
                                    strokes = strokes,
                                    fills = fills,
                                    stickers = stickers,
                                    triggerRefresh = 0L,
                                    onAddStroke = {},
                                    onAddFill = { _, _, _ -> },
                                    onPickColor = {}
                                )

                                if (isCompleted) {
                                    Surface(
                                        color = Color(0xFF4CAF50),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = "Done",
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = "Done",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.toggleFavorite(page.id, isFav) },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Favorite",
                                        tint = if (isFav) Color(0xFFFF5252) else Color.Gray
                                    )
                                }
                            }

                            // Card Info
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = page.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Page ${page.pageNumber}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                LinearProgressIndicator(
                                    progress = { progress / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = Color(0xFFEFE8D8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
