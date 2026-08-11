package com.virtualcamera.app.media

import android.content.Context
import android.net.Uri
import android.view.Surface
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExoPlayerWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private var isPlaying = false

    @OptIn(UnstableApi::class)
    fun prepare(url: String, surface: Surface, loop: Boolean = true) {
        release()

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            setVideoSurface(surface)
            repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            setMediaItem(mediaItem)
            prepare()
            play()
        }
        isPlaying = true
    }

    fun release() {
        exoPlayer?.let {
            if (isPlaying) {
                it.stop()
            }
            it.release()
        }
        exoPlayer = null
        isPlaying = false
    }

    fun setVolume(volume: Float) {
        exoPlayer?.volume = volume
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying
}
