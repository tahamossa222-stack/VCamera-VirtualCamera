package com.virtualcamera.app.media

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.view.Surface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaCodecDecoder @Inject constructor() {

    private var codec: MediaCodec? = null
    private var extractor: MediaExtractor? = null
    private var decodeJob: Job? = null

    fun decode(filePath: String, surface: Surface) {
        stop()

        extractor = MediaExtractor().apply {
            setDataSource(filePath)
        }

        val format = extractor?.getTrackFormat(0) ?: return
        val mime = format.getString(MediaFormat.KEY_MIME) ?: return

        codec = MediaCodec.createDecoderByType(mime).apply {
            configure(format, surface, null, 0)
            start()
        }

        decodeJob = CoroutineScope(Dispatchers.IO).launch {
            decodeLoop()
        }
    }

    private fun decodeLoop() {
        val bufferInfo = MediaCodec.BufferInfo()
        val extractor = extractor ?: return
        val codec = codec ?: return

        while (isActive) {
            val inputIndex = codec.dequeueInputBuffer(10_000)
            if (inputIndex >= 0) {
                val inputBuffer = codec.getInputBuffer(inputIndex) ?: continue
                val sampleSize = extractor.readSampleData(inputBuffer, 0)
                if (sampleSize < 0) {
                    codec.queueInputBuffer(inputIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                    break
                }
                codec.queueInputBuffer(inputIndex, 0, sampleSize, extractor.sampleTime, 0)
                extractor.advance()
            }

            val outputIndex = codec.dequeueOutputBuffer(bufferInfo, 10_000)
            if (outputIndex >= 0) {
                codec.releaseOutputBuffer(outputIndex, bufferInfo.size > 0)
            }
        }
    }

    fun stop() {
        decodeJob?.cancel()
        decodeJob = null
        codec?.stop()
        codec?.release()
        codec = null
        extractor?.release()
        extractor = null
    }
}
