package com.sport.timervideosport

// Заглушка для iOS — пока не реализована
actual fun rememberVideoRecorder(): VideoRecorder {
    return object : VideoRecorder {
        override val isRecording = kotlinx.coroutines.flow.MutableStateFlow(false)

        override suspend fun start() {
            // ничего не делаем
        }

        override suspend fun stop() {
            // ничего не делаем
        }
    }
}