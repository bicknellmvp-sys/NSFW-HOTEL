package com.example.model

import androidx.compose.ui.graphics.Color

enum class ColoringTool(
    val title: String,
    val iconRes: String,
    val description: String
) {
    BUCKET("Tap Fill", "ic_bucket", "Fill closed areas instantly with tap"),
    BRUSH("Paint Brush", "ic_brush", "Smooth expressive freehand strokes"),
    PENCIL("Fine Pencil", "ic_pencil", "Precision thin pencil for detail"),
    MARKER("Bold Marker", "ic_marker", "Vivid opaque marker lines"),
    GLOW("Glow Pen", "ic_glow", "Luminous neon glowing brush"),
    ERASER("Eraser", "ic_eraser", "Clean up freehand strokes"),
    EYEDROPPER("Color Picker", "ic_pipette", "Sample any color from canvas"),
    STICKER("Sticker", "ic_sticker", "Place cute stickers & sparkles")
}

data class ColorPalette(
    val name: String,
    val colors: List<Color>
) {
    companion object {
        val PASTEL = ColorPalette(
            "Pastel Dream",
            listOf(
                Color(0xFFFFB7B2), Color(0xFFFFDAC1), Color(0xFFE2F0CB),
                Color(0xFFB5EAD7), Color(0xFFC7CEEA), Color(0xFFF9C80E),
                Color(0xFFEA3546), Color(0xFFF8A271), Color(0xFF98DDCA),
                Color(0xFFD5AAFF), Color(0xFFFFC6FF), Color(0xFFBDB2FF)
            )
        )

        val VIVID = ColorPalette(
            "Vivid Pop",
            listOf(
                Color(0xFFFF0055), Color(0xFFFF5000), Color(0xFFFFCC00),
                Color(0xFF00E676), Color(0xFF00B0FF), Color(0xFF651FFF),
                Color(0xFFF50057), Color(0xFFD500F9), Color(0xFF00E5FF),
                Color(0xFF76FF03), Color(0xFFFFAB00), Color(0xFFFF3D00)
            )
        )

        val EARTH = ColorPalette(
            "Warm Earth",
            listOf(
                Color(0xFF8D5B4C), Color(0xFFA57562), Color(0xFFC89B7B),
                Color(0xFFE8D3B9), Color(0xFF556B2F), Color(0xFF6B8E23),
                Color(0xFF8FBC8F), Color(0xFF2F4F4F), Color(0xFF708090),
                Color(0xFFD2691E), Color(0xFFCD853F), Color(0xFFDEB887)
            )
        )

        val SUNSET = ColorPalette(
            "Sunset Glow",
            listOf(
                Color(0xFF2C1338), Color(0xFF4F1A46), Color(0xFF7C238C),
                Color(0xFFB82C77), Color(0xFFE24357), Color(0xFFFA6838),
                Color(0xFFFF9438), Color(0xFFFFBF42), Color(0xFFFFE066),
                Color(0xFFFF6F59), Color(0xFF251351), Color(0xFF13093A)
            )
        )

        val OCEAN = ColorPalette(
            "Deep Ocean",
            listOf(
                Color(0xFF03045E), Color(0xFF023E8A), Color(0xFF0077B6),
                Color(0xFF0096C7), Color(0xFF00B4D8), Color(0xFF48CAE4),
                Color(0xFF90E0EF), Color(0xFFADE8F4), Color(0xFFCAF0F8),
                Color(0xFF1D3557), Color(0xFF457B9D), Color(0xFFA8DADC)
            )
        )

        val RAINBOW = ColorPalette(
            "Rainbow Fun",
            listOf(
                Color(0xFFFF0000), Color(0xFFFF7F00), Color(0xFFFFFF00),
                Color(0xFF00FF00), Color(0xFF0000FF), Color(0xFF4B0082),
                Color(0xFF9400D3), Color(0xFFFF1493), Color(0xFF00FFFF),
                Color(0xFFFFD700), Color(0xFF00FF7F), Color(0xFFFF4500)
            )
        )

        val ALL_PALETTES = listOf(PASTEL, VIVID, EARTH, SUNSET, OCEAN, RAINBOW)
    }
}

data class StrokePoint(
    val x: Float,
    val y: Float
)

data class DrawStroke(
    val points: List<StrokePoint>,
    val color: Color,
    val strokeWidth: Float,
    val tool: ColoringTool,
    val alpha: Float = 1.0f
)

data class FillRecord(
    val x: Int,
    val y: Int,
    val color: Color
)

data class PlacedSticker(
    val id: String,
    val stickerEmoji: String,
    val x: Float,
    val y: Float,
    val scale: Float = 1.0f,
    val rotation: Float = 0f
)

data class Category(
    val id: String,
    val name: String,
    val iconEmoji: String,
    val badgeColorHex: Long
)

data class ColoringPage(
    val id: String,
    val title: String,
    val categoryId: String,
    val difficulty: String, // Easy, Medium, Detailed
    val description: String,
    val pageNumber: Int
)
