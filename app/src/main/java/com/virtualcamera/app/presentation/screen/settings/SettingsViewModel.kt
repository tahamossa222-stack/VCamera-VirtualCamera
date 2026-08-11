package com.virtualcamera.app.presentation.screen.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.virtualcamera.core.common.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SettingsUiState(
    val audioEnabled: Boolean = true,
    val loopVideo: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _uiState.value = SettingsUiState(
            audioEnabled = sharedPreferences.getBoolean(Constants.Preferences.KEY_AUDIO_ENABLED, true),
            loopVideo = sharedPreferences.getBoolean(Constants.Preferences.KEY_LOOP_VIDEO, true)
        )
    }

    fun toggleAudio(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.Preferences.KEY_AUDIO_ENABLED, enabled)
            .apply()
        _uiState.value = _uiState.value.copy(audioEnabled = enabled)
    }

    fun toggleLoop(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.Preferences.KEY_LOOP_VIDEO, enabled)
            .apply()
        _uiState.value = _uiState.value.copy(loopVideo = enabled)
    }
}
