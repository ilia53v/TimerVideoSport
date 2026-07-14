package com.sport.timervideosport.android

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool

class SoundPlayer(private val context: Context) {

    private var previousVolume: Int? = null

    val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val maxVolume =
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)


    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .build()

    private val sounds: Map<Sounds, Int> =
        Sounds.entries.associateWith {
            soundPool.load(context, it.resId, 1)
        }

    private var currentStreamId: Int? = null

    fun play(sound: Sounds) {

        stop() // ⬅️ важно: не даём звукам накладываться

        previousVolume =
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            maxVolume,
            0
        )

        val soundId = sounds[sound] ?: return

        currentStreamId = soundPool.play(
            soundId,
            1f,
            1f,
            1,
            0,
            1f
        )
    }

    fun stop() {

        previousVolume?.let {
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                it,
                0
            )
        }

        currentStreamId?.let {
            soundPool.stop(it)
            currentStreamId = null
        }
    }

    fun release() {
        stop()
        soundPool.release()
    }
}