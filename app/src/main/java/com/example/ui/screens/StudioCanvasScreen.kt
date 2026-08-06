package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.ColorPickerModalSheet
import com.example.ui.components.ColoringCanvas
import com.example.ui.components.PaletteAndToolBar
import com.example.ui.components.StickerOverlaySheet
import com.example.ui.viewmodel.ColoringViewModel

@Composable
fun StudioCanvasScreen(
    viewModel: ColoringViewModel,
    onBackToFlipbook: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activePage = viewModel.activePage
    val selectedTool by viewModel.selectedTool.collectAsState()
    val selectedColor by viewModel.selectedColor.collectAsState()
    val selectedPalette by viewModel.selectedPalette.collectAsState()
    val brushSize by viewModel.brushSize.collectAsState()
    val brushAlpha by viewModel.brushAlpha.collectAsState()
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val showColorPicker by viewModel.showColorPicker.collectAsState()
    val showStickerSheet by viewModel.showStickerSheet.collectAsState()
    val triggerRefresh by viewModel.triggerCanvasRefresh.collectAsState()

    val savedPages by viewModel.savedPages.collectAsState()
    val savedEntity = savedPages.find { it.pageId == activePage.id }
    val isFav = savedEntity?.isFavorite ?: false
    val isCompleted = savedEntity?.isCompleted ?: false

    val strokes = viewModel.getStrokesForPage(activePage.id)
    val fills = viewModel.getFillsForPage(activePage.id)
    val stickers = viewModel.getStickersForPage(activePage.id)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBackToFlipbook,
                            modifier = Modifier.testTag("back_to_flipbook_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Flipbook"
                            )
                        }
                        Column {
                            Text(
                                text = activePage.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Page ${activePage.pageNumber} • Studio Canvas",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Sound Toggle Button
                        IconButton(
                            onClick = { viewModel.toggleSound() },
                            modifier = Modifier.testTag("sound_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                                contentDescription = "Toggle Sound",
                                tint = if (soundEnabled) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        }

                        // Favorite Bookmark Button
                        IconButton(
                            onClick = { viewModel.toggleFavorite(activePage.id, isFav) },
                            modifier = Modifier.testTag("studio_favorite_button")
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favorite",
                                tint = if (isFav) Color(0xFFFF5252) else Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Complete Page Button
                        Button(
                            onClick = { viewModel.markPageCompleted(activePage.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("mark_completed_button")
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.AutoAwesome,
                                contentDescription = "Complete",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isCompleted) "Completed!" else "Finish",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Interactive Drawing Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ColoringCanvas(
                    pageId = activePage.id,
                    selectedTool = selectedTool,
                    selectedColor = selectedColor,
                    brushSize = brushSize,
                    brushAlpha = brushAlpha,
                    strokes = strokes,
                    fills = fills,
                    stickers = stickers,
                    triggerRefresh = triggerRefresh,
                    onAddStroke = { stroke -> viewModel.addStroke(stroke) },
                    onAddFill = { x, y, color -> viewModel.addFill(x, y, color) },
                    onPickColor = { color -> viewModel.selectColor(color) }
                )
            }

            // Bottom Tools & Palettes Bar
            PaletteAndToolBar(
                selectedTool = selectedTool,
                selectedColor = selectedColor,
                selectedPalette = selectedPalette,
                brushSize = brushSize,
                brushAlpha = brushAlpha,
                onSelectTool = { tool -> viewModel.selectTool(tool) },
                onSelectColor = { color -> viewModel.selectColor(color) },
                onSelectPalette = { palette -> viewModel.selectPalette(palette) },
                onBrushSizeChange = { size -> viewModel.setBrushSize(size) },
                onUndo = { viewModel.undo() },
                onRedo = { viewModel.redo() },
                onClear = { viewModel.clearCanvas() },
                onOpenColorPicker = { viewModel.openColorPicker() },
                onOpenStickerSheet = { viewModel.openStickerSheet() }
            )
        }

        // Modals
        if (showColorPicker) {
            ColorPickerModalSheet(
                currentColor = selectedColor,
                onColorPicked = { color -> viewModel.selectColor(color) },
                onDismiss = { viewModel.closeColorPicker() }
            )
        }

        if (showStickerSheet) {
            StickerOverlaySheet(
                onSelectSticker = { emoji -> viewModel.addSticker(emoji) },
                onDismiss = { viewModel.closeStickerSheet() }
            )
        }
    }
}
