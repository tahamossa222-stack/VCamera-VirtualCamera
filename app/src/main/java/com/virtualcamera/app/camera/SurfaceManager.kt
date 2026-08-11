package com.virtualcamera.app.camera

import android.graphics.SurfaceTexture
import android.view.Surface
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SurfaceManager @Inject constructor() {

    private val activeSurfaces = mutableMapOf<String, Surface>()
    private val surfaceTextures = mutableMapOf<String, SurfaceTexture>()

    fun createSurface(surfaceId: String, width: Int, height: Int): Surface? {
        return try {
            val surfaceTexture = SurfaceTexture(0).apply {
                setDefaultBufferSize(width, height)
            }
            val surface = Surface(surfaceTexture)

            surfaceTextures[surfaceId] = surfaceTexture
            activeSurfaces[surfaceId] = surface

            surface
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getSurface(surfaceId: String): Surface? {
        return activeSurfaces[surfaceId]
    }

    fun releaseSurface(surfaceId: String) {
        activeSurfaces.remove(surfaceId)
        surfaceTextures.remove(surfaceId)?.release()
    }

    fun releaseAll() {
        activeSurfaces.clear()
        surfaceTextures.values.forEach { it.release() }
        surfaceTextures.clear()
    }

    fun getSurfaceTexture(surfaceId: String): SurfaceTexture? {
        return surfaceTextures[surfaceId]
    }
}
