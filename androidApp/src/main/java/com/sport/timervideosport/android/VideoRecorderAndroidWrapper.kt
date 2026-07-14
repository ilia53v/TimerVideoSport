package com.sport.timervideosport.android

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.camera.video.Quality
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import com.sport.timervideosport.VideoRecorderAndroid
import kotlin.invoke

class VideoRecorderAndroidWrapper(context: Context) {

    private val recorder = VideoRecorderAndroid(context)

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun start(enableAudio: Boolean) {
        recorder.start(enableAudio)
    }

    fun stop() {
        recorder.stop()
    }

    fun buildVideoCapture(
        quality: Quality
    ) {
        recorder.buildVideoCapture(quality)
    }

    val videoCapture: VideoCapture<Recorder>
        get() = recorder.videoCapture
}