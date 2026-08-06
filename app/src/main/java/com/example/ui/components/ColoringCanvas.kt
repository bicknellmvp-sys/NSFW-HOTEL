package com.example.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.model.ColoringTool
import com.example.model.DrawStroke
import com.example.model.FillRecord
import com.example.model.PageTemplates
import com.example.model.PlacedSticker
import com.example.model.StrokePoint
import com.example.util.FloodFillUtil

@Composable
fun ColoringCanvas(
    pageId: String,
    selectedTool: ColoringTool,
    selectedColor: Color,
    brushSize: Float,
    brushAlpha: Float,
    strokes: List<DrawStroke>,
    fills: List<FillRecord>,
    stickers: List<PlacedSticker>,
    triggerRefresh: Long,
    onAddStroke: (DrawStroke) -> Unit,
    onAddFill: (x: Int, y: Int, color: Color) -> Unit,
    onPickColor: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    // Active freehand stroke points during touch drag
    var currentPoints by remember { mutableStateOf<List<StrokePoint>>(emptyList()) }

    // Bitmap cache for tap flood fills
    var bitmapCache by remember { mutableStateOf<Bitmap?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
            .pointerInput(pageId) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = (scale * zoom).coerceIn(1f, 5f)
                    if (scale > 1f) {
                        offsetX += pan.x
                        offsetY += pan.y
                    } else {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
            .pointerInput(selectedTool, pageId, triggerRefresh) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale > 1.5f) 1f else 2.5f
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onTap = { offset ->
                        if (selectedTool == ColoringTool.BUCKET) {
                            onAddFill(offset.x.toInt(), offset.y.toInt(), selectedColor)
                        } else if (selectedTool == ColoringTool.EYEDROPPER) {
                            bitmapCache?.let { bmp ->
                                val px = offset.x.toInt().coerceIn(0, bmp.width - 1)
                                val py = offset.y.toInt().coerceIn(0, bmp.height - 1)
                                val colorInt = bmp.getPixel(px, py)
                                onPickColor(Color(colorInt))
                            }
                        }
                    }
                )
            }
            .testTag("coloring_canvas_box")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(selectedTool, selectedColor, brushSize, brushAlpha, pageId) {
                    if (selectedTool != ColoringTool.BUCKET && selectedTool != ColoringTool.EYEDROPPER) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull() ?: continue
                                if (change.pressed) {
                                    val newPoint = StrokePoint(change.position.x, change.position.y)
                                    currentPoints = currentPoints + newPoint
                                    change.consume()
                                } else {
                                    if (currentPoints.isNotEmpty()) {
                                        val newStroke = DrawStroke(
                                            points = currentPoints,
                                            color = selectedColor,
                                            strokeWidth = brushSize,
                                            tool = selectedTool,
                                            alpha = brushAlpha
                                        )
                                        onAddStroke(newStroke)
                                        currentPoints = emptyList()
                                    }
                                }
                            }
                        }
                    }
                }
        ) {
            val canvasWidth = size.width.toInt().coerceAtLeast(100)
            val canvasHeight = size.height.toInt().coerceAtLeast(100)

            // Rebuild bitmap for flood fills if needed
            if (bitmapCache == null || bitmapCache?.width != canvasWidth || bitmapCache?.height != canvasHeight) {
                val newBmp = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
                newBmp.eraseColor(android.graphics.Color.WHITE)
                bitmapCache = newBmp
            }

            val bmp = bitmapCache
            if (bmp != null) {
                // Apply fills onto bitmap
                for (fill in fills) {
                    FloodFillUtil.floodFill(
                        bmp,
                        fill.x.coerceIn(0, canvasWidth - 1),
                        fill.y.coerceIn(0, canvasHeight - 1),
                        bmp.getPixel(fill.x.coerceIn(0, canvasWidth - 1), fill.y.coerceIn(0, canvasHeight - 1)),
                        fill.color.toArgb()
                    )
                }
                drawImage(bmp.asImageBitmap())
            }

            // Draw historical freehand strokes
            for (stroke in strokes) {
                if (stroke.points.size > 1) {
                    val path = Path().apply {
                        moveTo(stroke.points[0].x, stroke.points[0].y)
                        for (i in 1 until stroke.points.size) {
                            lineTo(stroke.points[i].x, stroke.points[i].y)
                        }
                    }
                    val drawColor = if (stroke.tool == ColoringTool.ERASER) Color.White else stroke.color.copy(alpha = stroke.alpha)
                    val capStyle = if (stroke.tool == ColoringTool.PENCIL) StrokeCap.Square else StrokeCap.Round

                    if (stroke.tool == ColoringTool.GLOW) {
                        // Outer glow layer
                        drawPath(
                            path = path,
                            color = stroke.color.copy(alpha = 0.3f),
                            style = Stroke(width = stroke.strokeWidth * 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )
                    }

                    drawPath(
                        path = path,
                        color = drawColor,
                        style = Stroke(width = stroke.strokeWidth, cap = capStyle, join = StrokeJoin.Round)
                    )
                }
            }

            // Draw active freehand stroke in progress
            if (currentPoints.size > 1) {
                val activePath = Path().apply {
                    moveTo(currentPoints[0].x, currentPoints[0].y)
                    for (i in 1 until currentPoints.size) {
                        lineTo(currentPoints[i].x, currentPoints[i].y)
                    }
                }
                val activeColor = if (selectedTool == ColoringTool.ERASER) Color.White else selectedColor.copy(alpha = brushAlpha)
                drawPath(
                    path = activePath,
                    color = activeColor,
                    style = Stroke(width = brushSize, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            // Render vector line-art outlines crisp on top!
            PageTemplates.drawPageOutlines(this, pageId, strokeWidthPx = 6f)
        }

        // Render Stickers & Sparkles overlay
        for (sticker in stickers) {
            Text(
                text = sticker.stickerEmoji,
                fontSize = 42.sp,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = sticker.x
                        translationY = sticker.y
                    }
            )
        }
    }
}
