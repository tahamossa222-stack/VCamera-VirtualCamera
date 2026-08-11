package com.virtualcamera.app.camera

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VirtualCameraEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var cameraThread: HandlerThread? = null
    private var cameraHandler: Handler? = null
    private var fakeSurface: Surface? = null

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    var isActive: Boolean = false
        private set

    fun start(fakeSurface: Surface) {
        this.fakeSurface = fakeSurface
        cameraThread = HandlerThread("VirtualCameraThread").apply { start() }
        cameraHandler = Handler(cameraThread!!.looper)
        isActive = true
    }

    fun stop() {
        isActive = false
        captureSession?.close()
        cameraDevice?.close()
        cameraThread?.quitSafely()
        cameraThread = null
        cameraHandler = null
        fakeSurface = null
    }

    fun getCameraIds(): Array<String> {
        return cameraManager.cameraIdList
    }

    fun openCamera(cameraId: String, surface: Surface) {
        try {
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createCaptureSession(surface)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    cameraDevice = null
                }
            }, cameraHandler)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun createCaptureSession(surface: Surface) {
        val camera = cameraDevice ?: return
        try {
            captureRequestBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)
            captureRequestBuilder?.addTarget(surface)

            camera.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        session.setRepeatingRequest(
                            captureRequestBuilder!!.build(),
                            null,
                            cameraHandler
                        )
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        session.close()
                    }
                },
                cameraHandler
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getCameraCharacteristics(cameraId: String): android.hardware.camera2.CameraCharacteristics? {
        return try {
            cameraManager.getCameraCharacteristics(cameraId)
        } catch (e: Exception) {
            null
        }
    }
}
