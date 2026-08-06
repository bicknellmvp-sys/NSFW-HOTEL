package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerModalSheet(
    currentColor: Color,
    onColorPicked: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var red by remember { mutableFloatStateOf(currentColor.red) }
    var green by remember { mutableFloatStateOf(currentColor.green) }
    var blue by remember { mutableFloatStateOf(currentColor.blue) }

    val activeColor = Color(red, green, blue)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Custom Color Picker",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Live Color Preview Circle
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(activeColor)
                    .border(2.dp, Color.LightGray, CircleShape)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Red Slider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(text = "R", fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.padding(end = 8.dp))
                Slider(
                    value = red,
                    onValueChange = { red = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("red_slider")
                )
            }

            // Green Slider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(text = "G", fontWeight = FontWeight.Bold, color = Color.Green, modifier = Modifier.padding(end = 8.dp))
                Slider(
                    value = green,
                    onValueChange = { green = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("green_slider")
                )
            }

            // Blue Slider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text(text = "B", fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.padding(end = 8.dp))
                Slider(
                    value = blue,
                    onValueChange = { blue = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("blue_slider")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onColorPicked(activeColor)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("select_color_confirm_button")
            ) {
                Text("Select Color")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
