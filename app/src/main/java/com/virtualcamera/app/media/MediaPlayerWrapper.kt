package com.virtualcamera.app.media

import android.media.MediaPlayer
import android.view.Surface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerWrapper @Inject constructor() {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    fun prepare(url: String, surface: Surface, loop: Boolean = true) {
        release()

        mediaPlayer = MediaPlayer().apply {
            setSurface(surface)
            setDataSource(url)
            isLooping = loop
            setOnPreparedListener {
                start()
                isPlaying = true
            }
            setOnCompletionListener {
                isPlaying = false
            }
            setOnErrorListener { _, _, _ ->
                isPlaying = false
                true
            }
            prepareAsync()
        }
    }

    fun release() {
        mediaPlayer?.let {
            if (isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        isPlaying = false
    }

    fun setVolume(leftVolume: Float, rightVolume: Float) {
        mediaPlayer?.setVolume(leftVolume, rightVolume)
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying
}
