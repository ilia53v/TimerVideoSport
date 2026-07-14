package com.sport.timervideosport.android

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VideoQualitySelector(
    selected: VideoQuality,
    onSelected: (VideoQuality) -> Unit,
    accentColor: Color
) {
    Column {
        VideoQuality.entries.forEach { quality ->

            val isSelected = quality == selected

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isSelected)
                            accentColor
                        else
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    )
                    .border(
                        1.dp,
                        accentColor
                    )
                    .clickable {
                        onSelected(quality)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = quality.name,
                    color = if (isSelected)
                        Color.White
                    else
                        accentColor
                )
            }
        }
    }
}