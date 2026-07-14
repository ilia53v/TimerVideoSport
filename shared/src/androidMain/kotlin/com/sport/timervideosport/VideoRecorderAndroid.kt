package com.sport.timervideosport

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.camera.video.*
import androidx.core.content.ContextCompat


class VideoRecorderAndroid(
    private val context: Context
) {
    private var currentQuality: Quality? = null
    private lateinit var recorder: Recorder

    lateinit var videoCapture: VideoCapture<Recorder>
        private set


    fun buildVideoCapture(quality: Quality) {

        if (currentQuality == quality && ::videoCapture.isInitialized)
            return

        currentQuality = quality

        recorder = Recorder.Builder()
            .setQualitySelector(
                QualitySelector.from(
                    quality,
                    FallbackStrategy.lowerQualityOrHigherThan(quality)
                )
            )
            .build()

        videoCapture = VideoCapture.withOutput(recorder)
    }

    private var recording: Recording? = null

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun start(enableAudio: Boolean
    ) {
        val name = "VID_${System.currentTimeMillis()}.mp4"

        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/TimerVideos")
        }

        val outputOptions = MediaStoreOutputOptions
            .Builder(
                context.contentResolver,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            )
            .setContentValues(contentValues)
            .build()

        var pending = recorder.prepareRecording(context, outputOptions)

        if (enableAudio &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pending = pending.withAudioEnabled()
        }

        recording = pending.start(
            ContextCompat.getMainExecutor(context)
        ) {}
    }

    fun stop() {
        recording?.stop()
        recording = null
    }
}

// actual для shared
actual fun rememberVideoRecorder(): VideoRecorder {
    error("Must be called only from Android via androidApp module")
}