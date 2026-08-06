package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.screens.FlipBookScreen
import com.example.ui.screens.GalleryScreen
import com.example.ui.screens.StudioCanvasScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ColoringViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: ColoringViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        ColoringBookApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun ColoringBookApp(viewModel: ColoringViewModel) {
  val currentMode by viewModel.currentMode.collectAsState()

  Scaffold(
    bottomBar = {
      // Modern M3 Navigation Bar for primary views
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
      ) {
        NavigationBarItem(
          selected = currentMode == "FLIPBOOK",
          onClick = { viewModel.setMode("FLIPBOOK") },
          icon = {
            Icon(
              imageVector = if (currentMode == "FLIPBOOK") Icons.Filled.Book else Icons.Outlined.Book,
              contentDescription = "Flip Book"
            )
          },
          label = { Text("Flip Book") },
          modifier = Modifier.testTag("nav_flipbook")
        )

        NavigationBarItem(
          selected = currentMode == "STUDIO",
          onClick = { viewModel.setMode("STUDIO") },
          icon = {
            Icon(
              imageVector = if (currentMode == "STUDIO") Icons.Filled.ColorLens else Icons.Outlined.ColorLens,
              contentDescription = "Color Studio"
            )
          },
          label = { Text("Studio") },
          modifier = Modifier.testTag("nav_studio")
        )

        NavigationBarItem(
          selected = currentMode == "GALLERY",
          onClick = { viewModel.setMode("GALLERY") },
          icon = {
            Icon(
              imageVector = if (currentMode == "GALLERY") Icons.Filled.Collections else Icons.Outlined.Collections,
              contentDescription = "Gallery"
            )
          },
          label = { Text("Gallery") },
          modifier = Modifier.testTag("nav_gallery")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentMode) {
        "FLIPBOOK" -> {
          FlipBookScreen(
            viewModel = viewModel,
            onOpenStudio = { viewModel.setMode("STUDIO") }
          )
        }
        "STUDIO" -> {
          StudioCanvasScreen(
            viewModel = viewModel,
            onBackToFlipbook = { viewModel.setMode("FLIPBOOK") }
          )
        }
        "GALLERY" -> {
          GalleryScreen(
            viewModel = viewModel,
            onOpenPageInBook = { pageIdx ->
              viewModel.selectPageIndex(pageIdx)
              viewModel.setMode("FLIPBOOK")
            }
          )
        }
      }
    }
  }
}
