package com.example.util

import android.graphics.Bitmap
import android.graphics.Color
import java.util.ArrayDeque

/**
 * Fast Queue-based Flood Fill algorithm for bucket coloring on Bitmap canvas.
 */
object FloodFillUtil {

    /**
     * Fills connected pixels of target color with replacement color.
     * @param bitmap Target bitmap to modify in-place
     * @param x Starting X coordinate
     * @param y Starting Y coordinate
     * @param targetColor Original color at (x, y)
     * @param fillColor New color to fill
     * @param tolerance Maximum RGB difference tolerance (0-255)
     */
    fun floodFill(
        bitmap: Bitmap,
        x: Int,
        y: Int,
        targetColor: Int,
        fillColor: Int,
        tolerance: Int = 45
    ) {
        val width = bitmap.width
        val height = bitmap.height

        if (x !in 0 until width || y !in 0 until height) return
        if (colorMatch(targetColor, fillColor, 0)) return

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val targetPixel = pixels[y * width + x]
        if (!colorMatch(targetPixel, targetColor, tolerance)) return

        val queue = ArrayDeque<Point>()
        queue.add(Point(x, y))

        val visited = BooleanArray(width * height)

        while (!queue.isEmpty()) {
            val pt = queue.poll() ?: break
            val px = pt.x
            val py = pt.y

            if (px !in 0 until width || py !in 0 until height) continue
            val index = py * width + px

            if (visited[index]) continue
            visited[index] = true

            val currentPixel = pixels[index]

            // Stop if pixel doesn't match target or is a dark line boundary
            if (isBlackLine(currentPixel) || !colorMatch(currentPixel, targetColor, tolerance)) {
                continue
            }

            pixels[index] = fillColor

            // Add 4-directional neighbors
            if (px > 0 && !visited[index - 1]) queue.add(Point(px - 1, py))
            if (px < width - 1 && !visited[index + 1]) queue.add(Point(px + 1, py))
            if (py > 0 && !visited[index - width]) queue.add(Point(px, py - 1))
            if (py < height - 1 && !visited[index + width]) queue.add(Point(px, py + 1))
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    }

    private data class Point(val x: Int, val y: Int)

    private fun isBlackLine(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        val a = Color.alpha(color)
        // Dark line outline check
        return a > 180 && r < 50 && g < 50 && b < 50
    }

    private fun colorMatch(c1: Int, c2: Int, tolerance: Int): Boolean {
        if (c1 == c2) return true
        val rDiff = Math.abs(Color.red(c1) - Color.red(c2))
        val gDiff = Math.abs(Color.green(c1) - Color.green(c2))
        val bDiff = Math.abs(Color.blue(c1) - Color.blue(c2))
        val aDiff = Math.abs(Color.alpha(c1) - Color.alpha(c2))
        return rDiff <= tolerance && gDiff <= tolerance && bDiff <= tolerance && aDiff <= tolerance
    }
}
