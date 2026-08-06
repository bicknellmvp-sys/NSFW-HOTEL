package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_coloring_pages")
data class SavedPageEntity(
    @PrimaryKey val pageId: String,
    val title: String,
    val categoryId: String,
    val pageNumber: Int,
    val isCompleted: Boolean = false,
    val isFavorite: Boolean = false,
    val progressPercentage: Int = 0,
    val lastEditedTime: Long = System.currentTimeMillis(),
    val strokeCount: Int = 0,
    val customNotes: String = ""
)

@Entity(tableName = "favorite_swatches")
data class FavoriteSwatchEntity(
    @PrimaryKey val colorHex: String,
    val addedTime: Long = System.currentTimeMillis()
)
