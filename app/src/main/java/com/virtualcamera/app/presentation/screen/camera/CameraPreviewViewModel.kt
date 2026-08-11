package com.virtualcamera.app.presentation.screen.camera

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CameraPreviewUiState(
    val isPreviewing: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CameraPreviewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CameraPreviewUiState())
    val uiState: StateFlow<CameraPreviewUiState> = _uiState.asStateFlow()

    fun startPreview() {
        _uiState.value = _uiState.value.copy(isPreviewing = true)
    }

    fun stopPreview() {
        _uiState.value = _uiState.value.copy(isPreviewing = false)
    }
}
