package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Gesture
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ColorPalette
import com.example.model.ColoringTool

@Composable
fun PaletteAndToolBar(
    selectedTool: ColoringTool,
    selectedColor: Color,
    selectedPalette: ColorPalette,
    brushSize: Float,
    brushAlpha: Float,
    onSelectTool: (ColoringTool) -> Unit,
    onSelectColor: (Color) -> Unit,
    onSelectPalette: (ColorPalette) -> Unit,
    onBrushSizeChange: (Float) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    onOpenColorPicker: () -> Unit,
    onOpenStickerSheet: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBrushSliders by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFDF9))
            .border(1.dp, Color(0xFFEFE8D8))
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        // Expandable Brush Controls Slider
        if (showBrushSliders) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Size", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Slider(
                    value = brushSize,
                    onValueChange = onBrushSizeChange,
                    valueRange = 8f..80f,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .testTag("brush_size_slider")
                )
                Box(
                    modifier = Modifier
                        .size(brushSize.dp.coerceIn(12.dp, 32.dp))
                        .background(selectedColor, CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
        }

        // Row 1: Tools & Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                items(ColoringTool.values()) { tool ->
                    val isSelected = selectedTool == tool
                    Surface(
                        onClick = {
                            onSelectTool(tool)
                            if (tool == ColoringTool.BRUSH || tool == ColoringTool.GLOW || tool == ColoringTool.MARKER) {
                                showBrushSliders = !showBrushSliders
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF2EFE9),
                        shadowElevation = if (isSelected) 4.dp else 0.dp,
                        modifier = Modifier.testTag("tool_${tool.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val icon = when (tool) {
                                ColoringTool.BUCKET -> Icons.Default.FormatColorFill
                                ColoringTool.BRUSH -> Icons.Default.Brush
                                ColoringTool.PENCIL -> Icons.Default.Edit
                                ColoringTool.MARKER -> Icons.Default.Gesture
                                ColoringTool.GLOW -> Icons.Default.AutoAwesome
                                ColoringTool.ERASER -> Icons.Default.ColorLens
                                ColoringTool.EYEDROPPER -> Icons.Default.Palette
                                ColoringTool.STICKER -> Icons.Default.AutoAwesome
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = tool.title,
                                tint = if (isSelected) Color.White else Color(0xFF4A453E),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = tool.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else Color(0xFF4A453E),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Undo / Redo / Clear Actions
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onUndo, modifier = Modifier.testTag("undo_button")) {
                    Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo", tint = Color(0xFF4A453E))
                }
                IconButton(onClick = onRedo, modifier = Modifier.testTag("redo_button")) {
                    Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "Redo", tint = Color(0xFF4A453E))
                }
                IconButton(onClick = onClear, modifier = Modifier.testTag("clear_button")) {
                    Icon(Icons.Default.DeleteSweep, contentDescription = "Clear Canvas", tint = Color(0xFFFF5252))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: Palette Swatch Carousel
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Active Palette selector dropdown pills
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(selectedPalette.colors) { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                                shape = CircleShape
                            )
                            .clickable { onSelectColor(color) }
                            .testTag("color_swatch_${color.toArgb()}")
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Open HSL Color Picker Button
            IconButton(
                onClick = onOpenColorPicker,
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .testTag("custom_color_picker_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Custom Color",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Row 3: Palette Preset Selector Tabs
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
        ) {
            items(ColorPalette.ALL_PALETTES) { palette ->
                val isSelected = selectedPalette == palette
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectPalette(palette) },
                    label = { Text(text = palette.name, style = MaterialTheme.typography.labelSmall) },
                    modifier = Modifier.testTag("palette_tab_${palette.name.lowercase().replace(" ", "_")}")
                )
            }
        }
    }
}
