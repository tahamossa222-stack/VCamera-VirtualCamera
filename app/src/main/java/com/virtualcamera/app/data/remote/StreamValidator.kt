package com.virtualcamera.app.data.remote

import com.virtualcamera.app.domain.model.StreamProtocol
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class StreamValidator @Inject constructor() {

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val reason: String) : ValidationResult()
    }

    suspend fun validate(url: String): Result<ValidationResult> {
        return Result.runCatching {
            if (url.isBlank()) {
                return@runCatching ValidationResult.Invalid("URL cannot be empty")
            }

            val protocol = StreamProtocol.fromUrl(url)

            when (protocol) {
                StreamProtocol.UNKNOWN -> {
                    ValidationResult.Invalid("Unsupported protocol")
                }
                StreamProtocol.RTSP -> {
                    if (url.matches(Regex("^rtsp://[^\\s]+"))) {
                        ValidationResult.Valid
                    } else {
                        ValidationResult.Invalid("Invalid RTSP URL format")
                    }
                }
                StreamProtocol.HLS -> {
                    if (url.matches(Regex("^https?://[^\\s]+\\.m3u8[^\\s]*"))) {
                        ValidationResult.Valid
                    } else {
                        ValidationResult.Invalid("Invalid HLS URL format")
                    }
                }
                StreamProtocol.DASH -> {
                    if (url.matches(Regex("^https?://[^\\s]+\\.mpd[^\\s]*"))) {
                        ValidationResult.Valid
                    } else {
                        ValidationResult.Invalid("Invalid DASH URL format")
                    }
                }
                StreamProtocol.DIRECT -> {
                    if (url.matches(Regex("^https?://[^\\s]+"))) {
                        ValidationResult.Valid
                    } else {
                        ValidationResult.Invalid("Invalid URL format")
                    }
                }
            }
        }
    }
}
