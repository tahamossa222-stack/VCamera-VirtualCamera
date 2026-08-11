package com.virtualcamera.app.camera

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.view.Surface
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FrameInjector @Inject constructor(
    private val surfaceManager: SurfaceManager
) {

    fun injectFrame(surfaceId: String, imageProxy: ImageProxy) {
        val surface = surfaceManager.getSurface(surfaceId) ?: return
        val surfaceTexture = surfaceManager.getSurfaceTexture(surfaceId) ?: return

        try {
            val buffer = imageProxy.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            val bitmap = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            val canvas = Canvas()
            canvas.setBitmap(bitmap)

            surfaceTexture.updateTexImage()

            val gLDrawable = android.graphics.drawable.BitmapDrawable(null, bitmap)
            gLDrawable.setBounds(0, 0, bitmap.width, bitmap.height)
            gLDrawable.draw(canvas)

            canvas.setBitmap(null)
            bitmap.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun injectBitmap(surfaceId: String, bitmap: Bitmap) {
        val surfaceTexture = surfaceManager.getSurfaceTexture(surfaceId) ?: return

        try {
            val canvas = Canvas()
            canvas.drawBitmap(bitmap, Rect(0, 0, bitmap.width, bitmap.height), null)
            surfaceTexture.updateTexImage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearFrame(surfaceId: String) {
        val surfaceTexture = surfaceManager.getSurfaceTexture(surfaceId) ?: return
        try {
            surfaceTexture.updateTexImage()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
