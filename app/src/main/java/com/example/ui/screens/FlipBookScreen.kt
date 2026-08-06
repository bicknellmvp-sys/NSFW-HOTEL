package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.model.PageTemplates
import com.example.ui.components.ColoringCanvas
import com.example.ui.components.PageFlipLayout
import com.example.ui.viewmodel.ColoringViewModel

@Composable
fun FlipBookScreen(
    viewModel: ColoringViewModel,
    onOpenStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activePageIndex by viewModel.activePageIndex.collectAsState()
    val savedPages by viewModel.savedPages.collectAsState()
    val activePage = viewModel.activePage

    val savedEntity = savedPages.find { it.pageId == activePage.id }
    val isFav = savedEntity?.isFavorite ?: false
    val progress = savedEntity?.progressPercentage ?: 0

    val strokes = viewModel.getStrokesForPage(activePage.id)
    val fills = viewModel.getFillsForPage(activePage.id)
    val stickers = viewModel.getStickersForPage(activePage.id)
    val triggerRefresh by viewModel.triggerCanvasRefresh.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F5EE))
    ) {
        PageFlipLayout(
            page = activePage,
            totalPages = PageTemplates.PAGES.size,
            isFavorite = isFav,
            progressPercentage = progress,
            onNextPage = { viewModel.nextPage() },
            onPreviousPage = { viewModel.previousPage() },
            onOpenStudio = onOpenStudio,
            onToggleFavorite = { viewModel.toggleFavorite(activePage.id, isFav) }
        ) {
            // Read-only canvas preview inside flipbook
            ColoringCanvas(
                pageId = activePage.id,
                selectedTool = viewModel.selectedTool.value,
                selectedColor = viewModel.selectedColor.value,
                brushSize = viewModel.brushSize.value,
                brushAlpha = viewModel.brushAlpha.value,
                strokes = strokes,
                fills = fills,
                stickers = stickers,
                triggerRefresh = triggerRefresh,
                onAddStroke = {},
                onAddFill = { _, _, _ -> },
                onPickColor = {}
            )
        }
    }
}
