package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ColoringDao {
    @Query("SELECT * FROM saved_coloring_pages ORDER BY pageNumber ASC")
    fun getAllSavedPages(): Flow<List<SavedPageEntity>>

    @Query("SELECT * FROM saved_coloring_pages WHERE pageId = :pageId LIMIT 1")
    suspend fun getSavedPageById(pageId: String): SavedPageEntity?

    @Query("SELECT * FROM saved_coloring_pages WHERE isFavorite = 1 ORDER BY lastEditedTime DESC")
    fun getFavoritePages(): Flow<List<SavedPageEntity>>

    @Query("SELECT * FROM saved_coloring_pages WHERE isCompleted = 1 ORDER BY lastEditedTime DESC")
    fun getCompletedPages(): Flow<List<SavedPageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePage(page: SavedPageEntity)

    @Query("UPDATE saved_coloring_pages SET isFavorite = :isFavorite WHERE pageId = :pageId")
    suspend fun setFavorite(pageId: String, isFavorite: Boolean)

    @Query("UPDATE saved_coloring_pages SET isCompleted = :isCompleted, progressPercentage = :progress WHERE pageId = :pageId")
    suspend fun updateProgress(pageId: String, isCompleted: Boolean, progress: Int)

    @Query("DELETE FROM saved_coloring_pages WHERE pageId = :pageId")
    suspend fun resetPageData(pageId: String)

    @Query("SELECT * FROM favorite_swatches ORDER BY addedTime DESC LIMIT 20")
    fun getFavoriteSwatches(): Flow<List<FavoriteSwatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteSwatch(swatch: FavoriteSwatchEntity)
}
