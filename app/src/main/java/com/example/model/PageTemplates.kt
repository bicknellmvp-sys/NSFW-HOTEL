package com.example.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Defines Categories and 12 detailed Vector Line-Art Page Templates
 */
object PageTemplates {

    val CATEGORIES = listOf(
        Category("bondage", "Restraint & Bondage", "⛓️", 0xFFE50026),
        Category("occult", "Occult & Sigils", "👁️", 0xFFC9C6C5),
        Category("provocative", "Provocative Portraits", "🍷", 0xFFFFB3AE),
        Category("minimal", "Brutalist Minimalism", "🖤", 0xFF343535)
    )

    val PAGES = listOf(
        ColoringPage(
            id = "mandala_1",
            title = "Exh. 01 / Domination",
            categoryId = "bondage",
            difficulty = "Detailed",
            description = "High-difficulty intricate line art exploring tension and restraint.",
            pageNumber = 1
        ),
        ColoringPage(
            id = "sunflower_2",
            title = "Exh. 02 / Velvet Bondage",
            categoryId = "bondage",
            difficulty = "Intense",
            description = "Geometric rope and ribbon suspension studies.",
            pageNumber = 2
        ),
        ColoringPage(
            id = "fox_3",
            title = "Exh. 03 / Basement Protocol",
            categoryId = "provocative",
            difficulty = "Detailed",
            description = "Cinematic portrait with stark contrast and dramatic shadow play.",
            pageNumber = 3
        ),
        ColoringPage(
            id = "panda_4",
            title = "Exh. 04 / Solomonic Sigil",
            categoryId = "occult",
            difficulty = "Intense",
            description = "Esoteric ritual geometry and binding seals.",
            pageNumber = 4
        ),
        ColoringPage(
            id = "castle_5",
            title = "Exh. 05 / The Forbidden Veil",
            categoryId = "provocative",
            difficulty = "Medium",
            description = "Editorial silhouette study draped in shadow and silk.",
            pageNumber = 5
        ),
        ColoringPage(
            id = "dragon_6",
            title = "Exh. 06 / Gothic Rosette",
            categoryId = "occult",
            difficulty = "Detailed",
            description = "Complex architectural rose window with dark esoteric symmetry.",
            pageNumber = 6
        ),
        ColoringPage(
            id = "astronaut_7",
            title = "Exh. 07 / Leather & Ledger",
            categoryId = "bondage",
            difficulty = "Medium",
            description = "Minimalist tactile elements of control and precision.",
            pageNumber = 7
        ),
        ColoringPage(
            id = "galaxy_8",
            title = "Exh. 08 / Nocturnal Desire",
            categoryId = "provocative",
            difficulty = "Detailed",
            description = "High-contrast study of nocturnal shadows and contour.",
            pageNumber = 8
        ),
        ColoringPage(
            id = "owl_9",
            title = "Exh. 09 / Alchemical Circle",
            categoryId = "occult",
            difficulty = "Detailed",
            description = "Sacred geometry for ritual focus and meditation.",
            pageNumber = 9
        ),
        ColoringPage(
            id = "lotus_10",
            title = "Exh. 10 / Brutalist Silence",
            categoryId = "minimal",
            difficulty = "Easy",
            description = "Stark architectural minimalism and negative space.",
            pageNumber = 10
        ),
        ColoringPage(
            id = "unicorn_11",
            title = "Exh. 11 / Crimson Protocol",
            categoryId = "provocative",
            difficulty = "Intense",
            description = "Provocative line work for the discerning adult colorist.",
            pageNumber = 11
        ),
        ColoringPage(
            id = "mosaic_12",
            title = "Exh. 12 / The Left Hand",
            categoryId = "occult",
            difficulty = "Detailed",
            description = "Esoteric sigil representing the left-hand path of artistry.",
            pageNumber = 12
        )
    )

    /**
     * Draws vector line art outlines for each template onto a Compose DrawScope.
     */
    fun drawPageOutlines(drawScope: DrawScope, pageId: String, strokeWidthPx: Float = 5f) {
        val width = drawScope.size.width
        val height = drawScope.size.height
        val linePaint = Color.Black

        when (pageId) {
            "mandala_1" -> drawMandalaOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "sunflower_2" -> drawSunflowerOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "fox_3" -> drawFoxOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "panda_4" -> drawPandaOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "castle_5" -> drawCastleOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "dragon_6" -> drawDragonOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "astronaut_7" -> drawAstronautOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "galaxy_8" -> drawGalaxyOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "owl_9" -> drawOwlOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "lotus_10" -> drawLotusOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "unicorn_11" -> drawUnicornOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            "mosaic_12" -> drawMosaicOutlines(drawScope, width, height, strokeWidthPx, linePaint)
            else -> drawMandalaOutlines(drawScope, width, height, strokeWidthPx, linePaint)
        }
    }

    private fun drawMandalaOutlines(
        ds: DrawScope,
        w: Float,
        h: Float,
        sw: Float,
        color: Color
    ) {
        val center = Offset(w / 2f, h / 2f)
        val maxR = minOf(w, h) * 0.42f

        // Outer border
        ds.drawCircle(color, maxR, center, style = Stroke(sw))
        ds.drawCircle(color, maxR * 0.88f, center, style = Stroke(sw * 0.8f))
        ds.drawCircle(color, maxR * 0.65f, center, style = Stroke(sw * 0.8f))
        ds.drawCircle(color, maxR * 0.40f, center, style = Stroke(sw * 0.8f))
        ds.drawCircle(color, maxR * 0.18f, center, style = Stroke(sw * 0.8f))

        // Petal rings (12 petals)
        val petals = 12
        val angleStep = (2.0 * Math.PI / petals).toFloat()

        for (i in 0 until petals) {
            val a = i * angleStep
            val path = Path().apply {
                val rInner = maxR * 0.40f
                val rOuter = maxR * 0.65f
                val rMid = maxR * 0.52f
                val p1 = Offset(center.x + rInner * Math.cos(a.toDouble()).toFloat(), center.y + rInner * Math.sin(a.toDouble()).toFloat())
                val p2 = Offset(center.x + rOuter * Math.cos(a.toDouble()).toFloat(), center.y + rOuter * Math.sin(a.toDouble()).toFloat())
                val pControl = Offset(center.x + rMid * Math.cos(a + 0.3).toFloat(), center.y + rMid * Math.sin(a + 0.3).toFloat())
                moveTo(p1.x, p1.y)
                quadraticTo(pControl.x, pControl.y, p2.x, p2.y)
            }
            ds.drawPath(path, color, style = Stroke(sw))
        }

        for (i in 0 until petals) {
            val a = i * angleStep + (angleStep / 2f)
            val path = Path().apply {
                val rInner = maxR * 0.65f
                val rOuter = maxR * 0.88f
                val p1 = Offset(center.x + rInner * Math.cos(a.toDouble()).toFloat(), center.y + rInner * Math.sin(a.toDouble()).toFloat())
                val p2 = Offset(center.x + rOuter * Math.cos(a.toDouble()).toFloat(), center.y + rOuter * Math.sin(a.toDouble()).toFloat())
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
            }
            ds.drawPath(path, color, style = Stroke(sw))
        }
    }

    private fun drawSunflowerOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val center = Offset(w / 2f, h * 0.45f)
        val centerR = w * 0.16f

        // Center disk
        ds.drawCircle(color, centerR, center, style = Stroke(sw))

        // Grid in center disk
        for (i in -3..3) {
            val offset = i * (centerR / 3.5f)
            ds.drawLine(color, Offset(center.x - centerR + Math.abs(offset), center.y + offset), Offset(center.x + centerR - Math.abs(offset), center.y + offset), sw * 0.5f)
        }

        // Petals
        val petalCount = 16
        for (i in 0 until petalCount) {
            val angle = (2.0 * Math.PI * i / petalCount)
            val rOuter = centerR * 2.5f
            val tipX = center.x + rOuter * Math.cos(angle).toFloat()
            val tipY = center.y + rOuter * Math.sin(angle).toFloat()
            val base1X = center.x + centerR * Math.cos(angle - 0.15).toFloat()
            val base1Y = center.y + centerR * Math.sin(angle - 0.15).toFloat()
            val base2X = center.x + centerR * Math.cos(angle + 0.15).toFloat()
            val base2Y = center.y + centerR * Math.sin(angle + 0.15).toFloat()

            val petalPath = Path().apply {
                moveTo(base1X, base1Y)
                quadraticTo(
                    center.x + (centerR * 1.8f) * Math.cos(angle - 0.1).toFloat(),
                    center.y + (centerR * 1.8f) * Math.sin(angle - 0.1).toFloat(),
                    tipX, tipY
                )
                quadraticTo(
                    center.x + (centerR * 1.8f) * Math.cos(angle + 0.1).toFloat(),
                    center.y + (centerR * 1.8f) * Math.sin(angle + 0.1).toFloat(),
                    base2X, base2Y
                )
            }
            ds.drawPath(petalPath, color, style = Stroke(sw))
        }

        // Stem & Leaves
        val stemPath = Path().apply {
            moveTo(center.x, center.y + centerR * 2.5f)
            quadraticTo(center.x - 20f, h * 0.8f, center.x, h)
        }
        ds.drawPath(stemPath, color, style = Stroke(sw * 1.5f))

        // Leaf 1
        val leaf1 = Path().apply {
            moveTo(center.x - 10f, h * 0.72f)
            quadraticTo(center.x - w * 0.35f, h * 0.65f, center.x - w * 0.3f, h * 0.8f)
            quadraticTo(center.x - w * 0.15f, h * 0.82f, center.x - 10f, h * 0.76f)
        }
        ds.drawPath(leaf1, color, style = Stroke(sw))

        // Leaf 2
        val leaf2 = Path().apply {
            moveTo(center.x + 10f, h * 0.78f)
            quadraticTo(center.x + w * 0.35f, h * 0.72f, center.x + w * 0.28f, h * 0.86f)
            quadraticTo(center.x + w * 0.12f, h * 0.88f, center.x + 10f, h * 0.82f)
        }
        ds.drawPath(leaf2, color, style = Stroke(sw))
    }

    private fun drawFoxOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h / 2f

        // Fox Head
        val headPath = Path().apply {
            moveTo(cx - w * 0.25f, cy - h * 0.1f)
            lineTo(cx + w * 0.25f, cy - h * 0.1f)
            lineTo(cx, cy + h * 0.18f)
            close()
        }
        ds.drawPath(headPath, color, style = Stroke(sw))

        // Ears
        val leftEar = Path().apply {
            moveTo(cx - w * 0.22f, cy - h * 0.1f)
            lineTo(cx - w * 0.32f, cy - h * 0.28f)
            lineTo(cx - w * 0.08f, cy - h * 0.1f)
            close()
        }
        val rightEar = Path().apply {
            moveTo(cx + w * 0.22f, cy - h * 0.1f)
            lineTo(cx + w * 0.32f, cy - h * 0.28f)
            lineTo(cx + w * 0.08f, cy - h * 0.1f)
            close()
        }
        ds.drawPath(leftEar, color, style = Stroke(sw))
        ds.drawPath(rightEar, color, style = Stroke(sw))

        // Nose & Eyes
        ds.drawCircle(color, 16f, Offset(cx, cy + h * 0.16f))
        ds.drawCircle(color, 10f, Offset(cx - w * 0.1f, cy - h * 0.02f))
        ds.drawCircle(color, 10f, Offset(cx + w * 0.1f, cy - h * 0.02f))

        // Body
        val bodyPath = Path().apply {
            moveTo(cx - w * 0.15f, cy + h * 0.12f)
            quadraticTo(cx - w * 0.22f, cy + h * 0.32f, cx - w * 0.18f, cy + h * 0.38f)
            lineTo(cx + w * 0.18f, cy + h * 0.38f)
            quadraticTo(cx + w * 0.22f, cy + h * 0.32f, cx + w * 0.15f, cy + h * 0.12f)
        }
        ds.drawPath(bodyPath, color, style = Stroke(sw))

        // Mushrooms nearby
        ds.drawArc(color = color, startAngle = 180f, sweepAngle = 180f, useCenter = true, topLeft = Offset(cx - w * 0.38f, cy + h * 0.25f), size = Size(w * 0.18f, h * 0.12f), style = Stroke(sw))
        ds.drawLine(color, Offset(cx - w * 0.29f, cy + h * 0.31f), Offset(cx - w * 0.29f, cy + h * 0.39f), sw)
    }

    private fun drawPandaOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.45f

        // Head
        ds.drawCircle(color, w * 0.28f, Offset(cx, cy), style = Stroke(sw))

        // Ears
        ds.drawCircle(color, w * 0.08f, Offset(cx - w * 0.24f, cy - h * 0.15f), style = Stroke(sw))
        ds.drawCircle(color, w * 0.08f, Offset(cx + w * 0.24f, cy - h * 0.15f), style = Stroke(sw))

        // Eye Patches
        ds.drawOval(color, Offset(cx - w * 0.16f, cy - h * 0.03f), Size(w * 0.12f, h * 0.08f), style = Stroke(sw))
        ds.drawOval(color, Offset(cx + w * 0.04f, cy - h * 0.03f), Size(w * 0.12f, h * 0.08f), style = Stroke(sw))

        // Nose
        ds.drawOval(color, Offset(cx - w * 0.04f, cy + h * 0.08f), Size(w * 0.08f, h * 0.04f))

        // Body
        val body = Path().apply {
            moveTo(cx - w * 0.2f, cy + h * 0.2f)
            quadraticTo(cx - w * 0.3f, cy + h * 0.4f, cx, cy + h * 0.42f)
            quadraticTo(cx + w * 0.3f, cy + h * 0.4f, cx + w * 0.2f, cy + h * 0.2f)
        }
        ds.drawPath(body, color, style = Stroke(sw))

        // Bamboo stick
        ds.drawLine(color, Offset(cx - w * 0.35f, cy + h * 0.05f), Offset(cx - w * 0.1f, cy + h * 0.42f), sw * 1.8f)
    }

    private fun drawCastleOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val base = h * 0.85f

        // Main Wall
        val castleRect = Rect(cx - w * 0.3f, base - h * 0.35f, cx + w * 0.3f, base)
        ds.drawRect(color, castleRect.topLeft, castleRect.size, style = Stroke(sw))

        // Main Gate
        val gatePath = Path().apply {
            moveTo(cx - w * 0.08f, base)
            lineTo(cx - w * 0.08f, base - h * 0.12f)
            quadraticTo(cx, base - h * 0.18f, cx + w * 0.08f, base - h * 0.12f)
            lineTo(cx + w * 0.08f, base)
        }
        ds.drawPath(gatePath, color, style = Stroke(sw))

        // Center Tower
        val centerTower = Rect(cx - w * 0.12f, base - h * 0.55f, cx + w * 0.12f, base - h * 0.35f)
        ds.drawRect(color, centerTower.topLeft, centerTower.size, style = Stroke(sw))

        // Tower Cone
        val cone = Path().apply {
            moveTo(cx - w * 0.15f, base - h * 0.55f)
            lineTo(cx, base - h * 0.72f)
            lineTo(cx + w * 0.15f, base - h * 0.55f)
            close()
        }
        ds.drawPath(cone, color, style = Stroke(sw))

        // Flag
        ds.drawLine(color, Offset(cx, base - h * 0.72f), Offset(cx, base - h * 0.82f), sw)
        val flag = Path().apply {
            moveTo(cx, base - h * 0.82f)
            lineTo(cx + w * 0.12f, base - h * 0.78f)
            lineTo(cx, base - h * 0.74f)
            close()
        }
        ds.drawPath(flag, color, style = Stroke(sw))

        // Rainbow in background
        for (i in 0..3) {
            val r = w * (0.35f + i * 0.05f)
            ds.drawArc(color = color, startAngle = 180f, sweepAngle = 180f, useCenter = false, topLeft = Offset(cx - r, base - h * 0.7f - r * 0.5f), size = Size(r * 2f, r * 1.2f), style = Stroke(sw * 0.8f))
        }
    }

    private fun drawDragonOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.5f

        // Head
        val head = Path().apply {
            moveTo(cx - w * 0.15f, cy - h * 0.2f)
            quadraticTo(cx - w * 0.25f, cy - h * 0.15f, cx - w * 0.18f, cy - h * 0.08f)
            lineTo(cx, cy - h * 0.12f)
            close()
        }
        ds.drawPath(head, color, style = Stroke(sw))

        // Eye & Snout
        ds.drawCircle(color, 8f, Offset(cx - w * 0.1f, cy - h * 0.16f))

        // Body
        val body = Path().apply {
            moveTo(cx - w * 0.08f, cy - h * 0.08f)
            quadraticTo(cx - w * 0.22f, cy + h * 0.1f, cx - w * 0.1f, cy + h * 0.3f)
            quadraticTo(cx + w * 0.15f, cy + h * 0.32f, cx + w * 0.12f, cy + h * 0.05f)
            close()
        }
        ds.drawPath(body, color, style = Stroke(sw))

        // Wings
        val wing = Path().apply {
            moveTo(cx, cy - h * 0.05f)
            lineTo(cx + w * 0.3f, cy - h * 0.25f)
            lineTo(cx + w * 0.2f, cy - h * 0.02f)
            lineTo(cx + w * 0.32f, cy + h * 0.08f)
            lineTo(cx + w * 0.08f, cy + h * 0.05f)
            close()
        }
        ds.drawPath(wing, color, style = Stroke(sw))
    }

    private fun drawAstronautOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.45f

        // Helmet
        ds.drawCircle(color, w * 0.22f, Offset(cx, cy - h * 0.1f), style = Stroke(sw))
        // Visor
        ds.drawOval(color, Offset(cx - w * 0.14f, cy - h * 0.15f), Size(w * 0.28f, h * 0.12f), style = Stroke(sw))

        // Suit Body
        val suit = Path().apply {
            moveTo(cx - w * 0.18f, cy + h * 0.02f)
            lineTo(cx - w * 0.18f, cy + h * 0.25f)
            lineTo(cx + w * 0.18f, cy + h * 0.25f)
            lineTo(cx + w * 0.18f, cy + h * 0.02f)
            close()
        }
        ds.drawPath(suit, color, style = Stroke(sw))

        // Planet in corner
        val planetR = w * 0.12f
        val planetC = Offset(w * 0.2f, h * 0.18f)
        ds.drawCircle(color, planetR, planetC, style = Stroke(sw))
        ds.drawOval(color, Offset(planetC.x - planetR * 1.6f, planetC.y - planetR * 0.4f), Size(planetR * 3.2f, planetR * 0.8f), style = Stroke(sw))

        // Rocket ship
        val rocket = Path().apply {
            moveTo(w * 0.8f, h * 0.7f)
            lineTo(w * 0.75f, h * 0.85f)
            lineTo(w * 0.85f, h * 0.85f)
            close()
        }
        ds.drawPath(rocket, color, style = Stroke(sw))
    }

    private fun drawGalaxyOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h / 2f

        // Center Saturn
        val pr = w * 0.22f
        ds.drawCircle(color, pr, Offset(cx, cy), style = Stroke(sw))
        ds.drawOval(color, Offset(cx - pr * 1.8f, cy - pr * 0.4f), Size(pr * 3.6f, pr * 0.8f), style = Stroke(sw * 1.2f))

        // Stars
        val starCoords = listOf(
            Offset(w * 0.2f, h * 0.2f),
            Offset(w * 0.8f, h * 0.25f),
            Offset(w * 0.15f, h * 0.75f),
            Offset(w * 0.82f, h * 0.78f)
        )
        for (st in starCoords) {
            ds.drawCircle(color, 12f, st, style = Stroke(sw))
        }
    }

    private fun drawOwlOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.45f

        // Owl Head & Body
        val owlBody = Path().apply {
            moveTo(cx - w * 0.22f, cy - h * 0.15f)
            quadraticTo(cx, cy - h * 0.25f, cx + w * 0.22f, cy - h * 0.15f)
            quadraticTo(cx + w * 0.28f, cy + h * 0.15f, cx, cy + h * 0.28f)
            quadraticTo(cx - w * 0.28f, cy + h * 0.15f, cx - w * 0.22f, cy - h * 0.15f)
        }
        ds.drawPath(owlBody, color, style = Stroke(sw))

        // Eyes
        ds.drawCircle(color, w * 0.08f, Offset(cx - w * 0.1f, cy - h * 0.05f), style = Stroke(sw))
        ds.drawCircle(color, w * 0.08f, Offset(cx + w * 0.1f, cy - h * 0.05f), style = Stroke(sw))
        ds.drawCircle(color, 10f, Offset(cx - w * 0.1f, cy - h * 0.05f))
        ds.drawCircle(color, 10f, Offset(cx + w * 0.1f, cy - h * 0.05f))

        // Beak
        val beak = Path().apply {
            moveTo(cx - 15f, cy + 10f)
            lineTo(cx + 15f, cy + 10f)
            lineTo(cx, cy + 35f)
            close()
        }
        ds.drawPath(beak, color, style = Stroke(sw))

        // Branch
        ds.drawLine(color, Offset(0f, cy + h * 0.28f), Offset(w, cy + h * 0.28f), sw * 1.8f)

        // Moon
        ds.drawArc(color = color, startAngle = 40f, sweepAngle = 260f, useCenter = false, topLeft = Offset(w * 0.65f, h * 0.1f), size = Size(w * 0.25f, w * 0.25f), style = Stroke(sw))
    }

    private fun drawLotusOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.55f

        // Water Ripples
        for (i in 1..3) {
            ds.drawOval(color, Offset(cx - w * (0.15f * i), cy + h * (0.08f * i)), Size(w * (0.3f * i), h * (0.08f * i)), style = Stroke(sw * 0.8f))
        }

        // Lotus Center Petal
        val centerPetal = Path().apply {
            moveTo(cx, cy + 20f)
            quadraticTo(cx - w * 0.12f, cy - h * 0.12f, cx, cy - h * 0.22f)
            quadraticTo(cx + w * 0.12f, cy - h * 0.12f, cx, cy + 20f)
        }
        ds.drawPath(centerPetal, color, style = Stroke(sw))

        // Side Petals
        val leftPetal = Path().apply {
            moveTo(cx - 15f, cy + 20f)
            quadraticTo(cx - w * 0.25f, cy - h * 0.08f, cx - w * 0.18f, cy - h * 0.18f)
            quadraticTo(cx - w * 0.08f, cy - h * 0.12f, cx - 15f, cy + 20f)
        }
        val rightPetal = Path().apply {
            moveTo(cx + 15f, cy + 20f)
            quadraticTo(cx + w * 0.25f, cy - h * 0.08f, cx + w * 0.18f, cy - h * 0.18f)
            quadraticTo(cx + w * 0.08f, cy - h * 0.12f, cx + 15f, cy + 20f)
        }
        ds.drawPath(leftPetal, color, style = Stroke(sw))
        ds.drawPath(rightPetal, color, style = Stroke(sw))
    }

    private fun drawUnicornOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cx = w / 2f
        val cy = h * 0.5f

        // Head
        val head = Path().apply {
            moveTo(cx - w * 0.1f, cy - h * 0.12f)
            lineTo(cx + w * 0.15f, cy - h * 0.08f)
            quadraticTo(cx + w * 0.22f, cy + h * 0.02f, cx + w * 0.1f, cy + h * 0.08f)
            lineTo(cx - w * 0.15f, cy + h * 0.08f)
            close()
        }
        ds.drawPath(head, color, style = Stroke(sw))

        // Horn
        val horn = Path().apply {
            moveTo(cx - w * 0.05f, cy - h * 0.12f)
            lineTo(cx - w * 0.15f, cy - h * 0.32f)
            lineTo(cx, cy - h * 0.13f)
            close()
        }
        ds.drawPath(horn, color, style = Stroke(sw))

        // Eye
        ds.drawCircle(color, 8f, Offset(cx + w * 0.05f, cy - h * 0.04f))

        // Flowers
        ds.drawCircle(color, w * 0.06f, Offset(cx - w * 0.25f, cy + h * 0.2f), style = Stroke(sw))
        ds.drawCircle(color, w * 0.06f, Offset(cx + w * 0.25f, cy + h * 0.2f), style = Stroke(sw))
    }

    private fun drawMosaicOutlines(ds: DrawScope, w: Float, h: Float, sw: Float, color: Color) {
        val cols = 5
        val rows = 7
        val cellW = w / cols
        val cellH = h / rows

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val rect = Rect(c * cellW + 10f, r * cellH + 10f, (c + 1) * cellW - 10f, (r + 1) * cellH - 10f)
                ds.drawRect(color, rect.topLeft, rect.size, style = Stroke(sw))

                // Inner diagonal cross
                ds.drawLine(color, rect.topLeft, rect.bottomRight, sw * 0.6f)
            }
        }
    }
}
