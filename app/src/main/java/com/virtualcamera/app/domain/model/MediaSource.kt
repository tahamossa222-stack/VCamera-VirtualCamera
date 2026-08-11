package com.virtualcamera.app.domain.model

data class MediaSource(
    val id: Long = 0,
    val name: String,
    val path: String,
    val type: MediaType,
    val protocol: StreamProtocol? = null,
    val thumbnailPath: String? = null,
    val duration: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUsedAt: Long = System.currentTimeMillis()
) {
    val isStream: Boolean get() = type == MediaType.STREAM
    val isLocal: Boolean get() = type != MediaType.STREAM
    val displaySize: String?
        get() {
            if (width == null || height == null) return null
            return "${width}x${height}"
        }
}
