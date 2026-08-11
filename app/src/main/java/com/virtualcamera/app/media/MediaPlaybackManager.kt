package com.virtualcamera.app.media

import android.content.Context
import android.view.Surface
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.model.MediaType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlaybackManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exoPlayerWrapper: ExoPlayerWrapper,
    private val mediaPlayerWrapper: MediaPlayerWrapper,
    private val streamProtocolHandler: StreamProtocolHandler,
    private val mediaCodecDecoder: MediaCodecDecoder
) {
    private var currentSource: MediaSource? = null
    private var isPlaying = false

    fun play(source: MediaSource, surface: Surface, loop: Boolean = true, audioEnabled: Boolean = true) {
        stop()

        currentSource = source
        isPlaying = true

        when (source.type) {
            MediaType.VIDEO -> {
                if (source.isStream) {
                    val protocol = source.protocol ?: com.virtualcamera.app.domain.model.StreamProtocol.DIRECT
                    streamProtocolHandler.handleStream(
                        url = source.path,
                        surface = surface,
                        protocol = protocol,
                        loop = loop,
                        audioEnabled = audioEnabled
                    )
                } else {
                    val volume = if (audioEnabled) 1f else 0f
                    exoPlayerWrapper.prepare(source.path, surface, loop)
                    exoPlayerWrapper.setVolume(volume)
                }
            }
            MediaType.IMAGE -> {
                isPlaying = false
            }
            MediaType.STREAM -> {
                val protocol = source.protocol ?: com.virtualcamera.app.domain.model.StreamProtocol.DIRECT
                streamProtocolHandler.handleStream(
                    url = source.path,
                    surface = surface,
                    protocol = protocol,
                    loop = loop,
                    audioEnabled = audioEnabled
                )
            }
        }
    }

    fun stop() {
        exoPlayerWrapper.release()
        mediaPlayerWrapper.release()
        streamProtocolHandler.stop()
        mediaCodecDecoder.stop()
        currentSource = null
        isPlaying = false
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying

    fun getCurrentSource(): MediaSource? = currentSource
}
