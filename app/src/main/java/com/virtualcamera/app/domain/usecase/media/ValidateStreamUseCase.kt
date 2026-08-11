package com.virtualcamera.app.domain.usecase.media

import com.virtualcamera.app.domain.model.StreamProtocol
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class ValidateStreamUseCase @Inject constructor() {

    data class ValidationResult(
        val isValid: Boolean,
        val protocol: StreamProtocol,
        val error: String? = null
    )

    operator fun invoke(url: String): Result<ValidationResult> {
        return Result.runCatching {
            if (url.isBlank()) {
                return@runCatching ValidationResult(
                    isValid = false,
                    protocol = StreamProtocol.UNKNOWN,
                    error = "URL cannot be empty"
                )
            }

            val protocol = StreamProtocol.fromUrl(url)

            val isValid = when (protocol) {
                StreamProtocol.UNKNOWN -> false
                StreamProtocol.RTSP -> url.matches(Regex("^rtsp://.*"))
                StreamProtocol.HLS -> url.matches(Regex("^https?://.*\\.m3u8.*"))
                StreamProtocol.DASH -> url.matches(Regex("^https?://.*\\.mpd.*"))
                StreamProtocol.DIRECT -> url.matches(Regex("^https?://.*"))
            }

            val error = when {
                !isValid -> "Invalid URL format for ${protocol.name}"
                protocol == StreamProtocol.UNKNOWN -> "Unsupported stream protocol"
                else -> null
            }

            ValidationResult(
                isValid = isValid,
                protocol = protocol,
                error = error
            )
        }
    }
}
