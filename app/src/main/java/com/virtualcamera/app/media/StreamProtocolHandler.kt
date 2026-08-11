package com.virtualcamera.app.media

import android.view.Surface
import com.virtualcamera.app.domain.model.StreamProtocol
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamProtocolHandler @Inject constructor(
    private val exoPlayerWrapper: ExoPlayerWrapper,
    private val mediaPlayerWrapper: MediaPlayerWrapper
) {

    enum class PlayerType {
        EXO_PLAYER,
        MEDIA_PLAYER
    }

    fun handleStream(
        url: String,
        surface: Surface,
        protocol: StreamProtocol,
        loop: Boolean = true,
        audioEnabled: Boolean = true
    ) {
        val playerType = getPlayerType(protocol)

        when (playerType) {
            PlayerType.EXO_PLAYER -> {
                exoPlayerWrapper.prepare(url, surface, loop)
                exoPlayerWrapper.setVolume(if (audioEnabled) 1f else 0f)
            }
            PlayerType.MEDIA_PLAYER -> {
                val volume = if (audioEnabled) 1f else 0f
                mediaPlayerWrapper.prepare(url, surface, loop)
                mediaPlayerWrapper.setVolume(volume, volume)
            }
        }
    }

    fun stop() {
        exoPlayerWrapper.release()
        mediaPlayerWrapper.release()
    }

    private fun getPlayerType(protocol: StreamProtocol): PlayerType {
        return when (protocol) {
            StreamProtocol.RTSP -> PlayerType.EXO_PLAYER
            StreamProtocol.HLS -> PlayerType.EXO_PLAYER
            StreamProtocol.DASH -> PlayerType.EXO_PLAYER
            StreamProtocol.DIRECT -> PlayerType.MEDIA_PLAYER
            StreamProtocol.UNKNOWN -> PlayerType.MEDIA_PLAYER
        }
    }
}
