package com.example.data

import com.example.model.PageTemplates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ColoringRepository(private val dao: ColoringDao) {

    val allSavedPages: Flow<List<SavedPageEntity>> = dao.getAllSavedPages().map { list ->
        if (list.isEmpty()) {
            // Seed initial pages if database is empty
            seedInitialPages()
            emptyList()
        } else {
            list
        }
    }

    val favoritePages: Flow<List<SavedPageEntity>> = dao.getFavoritePages()
    val completedPages: Flow<List<SavedPageEntity>> = dao.getCompletedPages()
    val favoriteSwatches: Flow<List<FavoriteSwatchEntity>> = dao.getFavoriteSwatches()

    suspend fun getPageById(pageId: String): SavedPageEntity? {
        return dao.getSavedPageById(pageId)
    }

    suspend fun updateProgress(pageId: String, isCompleted: Boolean, progress: Int) {
        dao.updateProgress(pageId, isCompleted, progress)
    }

    suspend fun toggleFavorite(pageId: String, currentFavorite: Boolean) {
        dao.setFavorite(pageId, !currentFavorite)
    }

    suspend fun savePage(page: SavedPageEntity) {
        dao.insertOrUpdatePage(page)
    }

    suspend fun resetPage(pageId: String) {
        dao.resetPageData(pageId)
    }

    suspend fun addFavoriteSwatch(colorHex: String) {
        dao.addFavoriteSwatch(FavoriteSwatchEntity(colorHex = colorHex))
    }

    suspend fun seedInitialPages() {
        for (page in PageTemplates.PAGES) {
            val entity = SavedPageEntity(
                pageId = page.id,
                title = page.title,
                categoryId = page.categoryId,
                pageNumber = page.pageNumber,
                isCompleted = false,
                isFavorite = false,
                progressPercentage = 0,
                lastEditedTime = System.currentTimeMillis()
            )
            dao.insertOrUpdatePage(entity)
        }
    }
}
