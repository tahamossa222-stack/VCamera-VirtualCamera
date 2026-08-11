package com.virtualcamera.app.domain.usecase.camera

import android.content.SharedPreferences
import com.virtualcamera.core.common.Constants
import com.virtualcamera.core.common.Result
import javax.inject.Inject

class ToggleVirtualCameraUseCase @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(enabled: Boolean): Result<Unit> {
        return Result.runCatching {
            sharedPreferences.edit()
                .putBoolean(Constants.Preferences.KEY_VIRTUAL_CAMERA_ENABLED, enabled)
                .apply()
        }
    }

    fun isEnabled(): Boolean {
        return sharedPreferences.getBoolean(
            Constants.Preferences.KEY_VIRTUAL_CAMERA_ENABLED,
            false
        )
    }
}
