package com.virtualcamera.app.presentation.screen.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.model.MediaType
import com.virtualcamera.app.domain.model.StreamProtocol
import com.virtualcamera.app.domain.usecase.media.AddMediaSourceUseCase
import com.virtualcamera.app.domain.usecase.media.ValidateStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StreamInputUiState(
    val url: String = "",
    val selectedProtocol: StreamProtocol = StreamProtocol.RTSP,
    val isValidating: Boolean = false,
    val isValid: Boolean? = null,
    val validationError: String? = null,
    val isSaving: Boolean = false,
    val savedSuccessfully: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class StreamInputViewModel @Inject constructor(
    private val validateStreamUseCase: ValidateStreamUseCase,
    private val addMediaSourceUseCase: AddMediaSourceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StreamInputUiState())
    val uiState: StateFlow<StreamInputUiState> = _uiState.asStateFlow()

    fun updateUrl(url: String) {
        val protocol = StreamProtocol.fromUrl(url)
        _uiState.value = _uiState.value.copy(
            url = url,
            selectedProtocol = protocol,
            isValid = null,
            validationError = null
        )
    }

    fun selectProtocol(protocol: StreamProtocol) {
        _uiState.value = _uiState.value.copy(selectedProtocol = protocol)
    }

    fun validate() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isValidating = true)
            validateStreamUseCase(_uiState.value.url)
                .onSuccess { result ->
                    _uiState.value = _uiState.value.copy(
                        isValidating = false,
                        isValid = result.isValid,
                        validationError = result.error
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isValidating = false,
                        isValid = false,
                        validationError = e.message
                    )
                }
        }
    }

    fun saveStream() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val source = MediaSource(
                name = _uiState.value.url.substringAfterLast("/").ifBlank { "Stream" },
                path = _uiState.value.url,
                type = MediaType.STREAM,
                protocol = _uiState.value.selectedProtocol
            )
            addMediaSourceUseCase(source)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        savedSuccessfully = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = e.message
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
