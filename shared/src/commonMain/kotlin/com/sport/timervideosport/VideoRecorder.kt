package com.sport.timervideosport

import kotlinx.coroutines.flow.StateFlow


interface VideoRecorder {
    val isRecording: StateFlow<Boolean>

    suspend fun start()

    suspend fun stop()
}

expect fun rememberVideoRecorder(): VideoRecorder