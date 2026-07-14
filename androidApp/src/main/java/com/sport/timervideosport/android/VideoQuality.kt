package com.sport.timervideosport.android

import androidx.camera.video.Quality

enum class VideoQuality(
    val title: String,
    val quality: Quality
) {
    SD("SD (640×480)", Quality.SD),
    HD("HD (1280×720)", Quality.HD),
    FHD("Full HD (1920×1080)", Quality.FHD),
}