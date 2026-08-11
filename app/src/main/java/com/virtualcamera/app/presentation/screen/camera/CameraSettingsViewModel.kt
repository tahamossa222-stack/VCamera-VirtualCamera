package com.virtualcamera.app.presentation.screen.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualcamera.app.domain.model.CameraProfile
import com.virtualcamera.app.domain.usecase.camera.GetCameraProfilesUseCase
import com.virtualcamera.app.domain.usecase.camera.SwitchCameraProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CameraSettingsUiState(
    val isLoading: Boolean = true,
    val profiles: List<CameraProfile> = emptyList(),
    val activeProfile: CameraProfile? = null,
    val error: String? = null
)

@HiltViewModel
class CameraSettingsViewModel @Inject constructor(
    private val getCameraProfilesUseCase: GetCameraProfilesUseCase,
    private val switchCameraProfileUseCase: SwitchCameraProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraSettingsUiState())
    val uiState: StateFlow<CameraSettingsUiState> = _uiState.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            getCameraProfilesUseCase()
                .collect { profiles ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        profiles = profiles,
                        activeProfile = profiles.find { it.isActive }
                    )
                }
        }
    }

    fun switchProfile(profileId: Long) {
        viewModelScope.launch {
            when (val result = switchCameraProfileUseCase(profileId)) {
                is com.virtualcamera.core.common.Result.Failure -> {
                    _uiState.value = _uiState.value.copy(error = result.exception.message)
                }
                is com.virtualcamera.core.common.Result.Success -> { /* no-op */ }
            }
        }
    }
}
