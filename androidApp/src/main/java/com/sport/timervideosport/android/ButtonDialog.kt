package com.sport.timervideosport.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerButton(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.border(3.dp, Color.White, shape = CircleShape).size(50.dp),
        shape = CircleShape,
        colors = ButtonColors(
            selectedColor,
            selectedColor,
            selectedColor,
            selectedColor
        )
    ){}

    if (showDialog) {
        ColorPickerDialog(
            selectedColor = selectedColor,
            onColorSelected = {
                onColorSelected(it)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}