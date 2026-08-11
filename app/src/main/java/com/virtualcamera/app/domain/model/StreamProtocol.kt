package com.virtualcamera.app.domain.model

enum class StreamProtocol {
    RTSP,
    HLS,
    DASH,
    DIRECT,
    UNKNOWN;

    companion object {
        fun fromUrl(url: String): StreamProtocol {
            val lowerUrl = url.lowercase()
            return when {
                lowerUrl.startsWith("rtsp://") -> RTSP
                lowerUrl.startsWith("http") && lowerUrl.contains(".m3u8") -> HLS
                lowerUrl.startsWith("http") && lowerUrl.contains(".mpd") -> DASH
                lowerUrl.startsWith("http") || lowerUrl.startsWith("https") -> DIRECT
                else -> UNKNOWN
            }
        }
    }
}
