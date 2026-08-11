package com.virtualcamera.app.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.usecase.camera.GetCameraProfilesUseCase
import com.virtualcamera.app.domain.usecase.camera.ToggleVirtualCameraUseCase
import com.virtualcamera.app.domain.usecase.media.GetMediaSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val mediaSources: List<MediaSource> = emptyList(),
    val isVirtualCameraEnabled: Boolean = false,
    val currentSource: MediaSource? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMediaSourcesUseCase: GetMediaSourcesUseCase,
    private val toggleVirtualCameraUseCase: ToggleVirtualCameraUseCase,
    private val getCameraProfilesUseCase: GetCameraProfilesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMediaSources()
        checkVirtualCameraStatus()
    }

    private fun loadMediaSources() {
        viewModelScope.launch {
            getMediaSourcesUseCase()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { sources ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        mediaSources = sources
                    )
                }
        }
    }

    private fun checkVirtualCameraStatus() {
        _uiState.value = _uiState.value.copy(
            isVirtualCameraEnabled = toggleVirtualCameraUseCase.isEnabled()
        )
    }

    fun toggleVirtualCamera(enabled: Boolean) {
        toggleVirtualCameraUseCase(enabled)
        _uiState.value = _uiState.value.copy(
            isVirtualCameraEnabled = enabled
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
