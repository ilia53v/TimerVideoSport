package com.sport.timervideosport.android

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerDialog(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan,
        Color.Gray,
        Color(0xFF006400),
        Color(0xFFC71585),
        Color(0xFF1E90FF),
        Color(0xFFFF9800),
        Color(0xFFDAA520),
        Color(0xFF795548),
        Color(0xFF9C27B0),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Выберите цвет")
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4)
            ) {
                items(colors) { color ->
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 3.dp else 1.dp,
                                color = if (selectedColor == color)
                                    Color.White
                                else
                                    Color.Gray,
                                shape = CircleShape
                            )
                            .clickable {
                                onColorSelected(color)
                            }
                    )
                }
            }
        },
        confirmButton = {}
    )
}