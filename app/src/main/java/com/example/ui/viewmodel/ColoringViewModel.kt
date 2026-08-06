package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ColoringDatabase
import com.example.data.ColoringRepository
import com.example.data.SavedPageEntity
import com.example.model.ColorPalette
import com.example.model.ColoringPage
import com.example.model.ColoringTool
import com.example.model.DrawStroke
import com.example.model.FillRecord
import com.example.model.PageTemplates
import com.example.model.PlacedSticker
import com.example.util.SoundEffectsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ColoringViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ColoringRepository

    init {
        val database = ColoringDatabase.getInstance(application)
        repository = ColoringRepository(database.coloringDao())
        viewModelScope.launch {
            repository.seedInitialPages()
        }
    }

    val savedPages: StateFlow<List<SavedPageEntity>> = repository.allSavedPages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritePages: StateFlow<List<SavedPageEntity>> = repository.favoritePages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedPages: StateFlow<List<SavedPageEntity>> = repository.completedPages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Navigation / View Mode: "FLIPBOOK", "STUDIO", "GALLERY"
    private val _currentMode = MutableStateFlow("FLIPBOOK")
    val currentMode: StateFlow<String> = _currentMode.asStateFlow()

    // Active Page state
    private val _activePageIndex = MutableStateFlow(0)
    val activePageIndex: StateFlow<Int> = _activePageIndex.asStateFlow()

    val activePage: ColoringPage
        get() = PageTemplates.PAGES[_activePageIndex.value.coerceIn(0, PageTemplates.PAGES.lastIndex)]

    // Tools & Palettes
    private val _selectedTool = MutableStateFlow(ColoringTool.BUCKET)
    val selectedTool: StateFlow<ColoringTool> = _selectedTool.asStateFlow()

    private val _selectedColor = MutableStateFlow(ColorPalette.PASTEL.colors[0])
    val selectedColor: StateFlow<Color> = _selectedColor.asStateFlow()

    private val _selectedPalette = MutableStateFlow(ColorPalette.PASTEL)
    val selectedPalette: StateFlow<ColorPalette> = _selectedPalette.asStateFlow()

    private val _brushSize = MutableStateFlow(24f)
    val brushSize: StateFlow<Float> = _brushSize.asStateFlow()

    private val _brushAlpha = MutableStateFlow(1.0f)
    val brushAlpha: StateFlow<Float> = _brushAlpha.asStateFlow()

    // Stroke & Fill state per page (Key: pageId)
    private val pageStrokesMap = mutableMapOf<String, MutableList<DrawStroke>>()
    private val pageFillsMap = mutableMapOf<String, MutableList<FillRecord>>()
    private val pageRedoMap = mutableMapOf<String, MutableList<DrawStroke>>()
    private val pageStickersMap = mutableMapOf<String, MutableList<PlacedSticker>>()

    // UI state flags
    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _showColorPicker = MutableStateFlow(false)
    val showColorPicker: StateFlow<Boolean> = _showColorPicker.asStateFlow()

    private val _showStickerSheet = MutableStateFlow(false)
    val showStickerSheet: StateFlow<Boolean> = _showStickerSheet.asStateFlow()

    private val _triggerCanvasRefresh = MutableStateFlow(0L)
    val triggerCanvasRefresh: StateFlow<Long> = _triggerCanvasRefresh.asStateFlow()

    fun setMode(mode: String) {
        _currentMode.value = mode
    }

    fun selectPageIndex(index: Int) {
        if (index in 0 until PageTemplates.PAGES.size) {
            _activePageIndex.value = index
            SoundEffectsManager.playPageFlipSound()
        }
    }

    fun nextPage() {
        if (_activePageIndex.value < PageTemplates.PAGES.lastIndex) {
            _activePageIndex.value += 1
            SoundEffectsManager.playPageFlipSound()
        }
    }

    fun previousPage() {
        if (_activePageIndex.value > 0) {
            _activePageIndex.value -= 1
            SoundEffectsManager.playPageFlipSound()
        }
    }

    fun selectTool(tool: ColoringTool) {
        _selectedTool.value = tool
        if (tool == ColoringTool.STICKER) {
            _showStickerSheet.value = true
        }
    }

    fun selectColor(color: Color) {
        _selectedColor.value = color
    }

    fun selectPalette(palette: ColorPalette) {
        _selectedPalette.value = palette
        _selectedColor.value = palette.colors[0]
    }

    fun setBrushSize(size: Float) {
        _brushSize.value = size
    }

    fun setBrushAlpha(alpha: Float) {
        _brushAlpha.value = alpha
    }

    fun toggleSound() {
        val newSound = !_soundEnabled.value
        _soundEnabled.value = newSound
        SoundEffectsManager.soundEnabled = newSound
    }

    fun openColorPicker() {
        _showColorPicker.value = true
    }

    fun closeColorPicker() {
        _showColorPicker.value = false
    }

    fun openStickerSheet() {
        _showStickerSheet.value = true
    }

    fun closeStickerSheet() {
        _showStickerSheet.value = false
    }

    // Page Drawing Actions
    fun getStrokesForPage(pageId: String): List<DrawStroke> {
        return pageStrokesMap.getOrPut(pageId) { mutableListOf() }
    }

    fun getFillsForPage(pageId: String): List<FillRecord> {
        return pageFillsMap.getOrPut(pageId) { mutableListOf() }
    }

    fun getStickersForPage(pageId: String): List<PlacedSticker> {
        return pageStickersMap.getOrPut(pageId) { mutableListOf() }
    }

    fun addStroke(stroke: DrawStroke) {
        val pageId = activePage.id
        val list = pageStrokesMap.getOrPut(pageId) { mutableListOf() }
        list.add(stroke)
        pageRedoMap[pageId]?.clear()
        SoundEffectsManager.playBrushSound()
        updateProgressInDb(pageId)
        notifyCanvasChanged()
    }

    fun addFill(x: Int, y: Int, color: Color) {
        val pageId = activePage.id
        val list = pageFillsMap.getOrPut(pageId) { mutableListOf() }
        list.add(FillRecord(x, y, color))
        SoundEffectsManager.playPopSound()
        updateProgressInDb(pageId)
        notifyCanvasChanged()
    }

    fun addSticker(emoji: String, x: Float = 0.5f, y: Float = 0.5f) {
        val pageId = activePage.id
        val list = pageStickersMap.getOrPut(pageId) { mutableListOf() }
        val sticker = PlacedSticker(
            id = System.currentTimeMillis().toString(),
            stickerEmoji = emoji,
            x = x,
            y = y
        )
        list.add(sticker)
        SoundEffectsManager.playPopSound()
        notifyCanvasChanged()
    }

    fun undo() {
        val pageId = activePage.id
        val strokes = pageStrokesMap[pageId]
        val fills = pageFillsMap[pageId]

        if (!strokes.isNullOrEmpty()) {
            val removed = strokes.removeAt(strokes.lastIndex)
            val redoList = pageRedoMap.getOrPut(pageId) { mutableListOf() }
            redoList.add(removed)
            notifyCanvasChanged()
            return
        }

        if (!fills.isNullOrEmpty()) {
            fills.removeAt(fills.lastIndex)
            notifyCanvasChanged()
        }
    }

    fun redo() {
        val pageId = activePage.id
        val redoList = pageRedoMap[pageId]
        if (!redoList.isNullOrEmpty()) {
            val restored = redoList.removeAt(redoList.lastIndex)
            pageStrokesMap.getOrPut(pageId) { mutableListOf() }.add(restored)
            notifyCanvasChanged()
        }
    }

    fun clearCanvas() {
        val pageId = activePage.id
        pageStrokesMap[pageId]?.clear()
        pageFillsMap[pageId]?.clear()
        pageStickersMap[pageId]?.clear()
        pageRedoMap[pageId]?.clear()
        viewModelScope.launch {
            repository.updateProgress(pageId, false, 0)
        }
        notifyCanvasChanged()
    }

    fun toggleFavorite(pageId: String, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(pageId, currentFav)
        }
    }

    fun markPageCompleted(pageId: String) {
        viewModelScope.launch {
            repository.updateProgress(pageId, true, 100)
            SoundEffectsManager.playFanfareSound()
        }
    }

    private fun updateProgressInDb(pageId: String) {
        viewModelScope.launch {
            val strokeCount = (pageStrokesMap[pageId]?.size ?: 0) + (pageFillsMap[pageId]?.size ?: 0)
            val progress = minOf(100, strokeCount * 12)
            val isComplete = progress >= 90
            repository.updateProgress(pageId, isComplete, progress)
        }
    }

    private fun notifyCanvasChanged() {
        _triggerCanvasRefresh.value = System.currentTimeMillis()
    }
}
