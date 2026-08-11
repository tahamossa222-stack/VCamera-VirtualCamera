package com.virtualcamera.app.presentation.screen.media

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.model.MediaType
import com.virtualcamera.app.domain.usecase.media.AddMediaSourceUseCase
import com.virtualcamera.app.domain.usecase.media.GetMediaSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaPickerUiState(
    val isLoading: Boolean = false,
    val mediaSources: List<MediaSource> = emptyList(),
    val selectedMediaType: MediaType? = null,
    val error: String? = null
)

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getMediaSourcesUseCase: GetMediaSourcesUseCase,
    private val addMediaSourceUseCase: AddMediaSourceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaPickerUiState())
    val uiState: StateFlow<MediaPickerUiState> = _uiState.asStateFlow()

    init {
        loadMediaSources()
    }

    private fun loadMediaSources() {
        viewModelScope.launch {
            getMediaSourcesUseCase()
                .collect { sources ->
                    _uiState.value = _uiState.value.copy(mediaSources = sources)
                }
        }
    }

    fun selectMediaType(type: MediaType?) {
        _uiState.value = _uiState.value.copy(selectedMediaType = type)
    }

    fun addMediaFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val fileName = getFileName(uri)
                val mediaSource = MediaSource(
                    name = fileName,
                    path = uri.toString(),
                    type = getMediaTypeFromUri(uri)
                )
                addMediaSourceUseCase(mediaSource)
                    .onSuccess { loadMediaSources() }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(error = e.message)
                    }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex) ?: "Unknown"
        } ?: "Unknown"
    }

    private fun getMediaTypeFromUri(uri: Uri): MediaType {
        val mimeType = context.contentResolver.getType(uri)
        return when {
            mimeType?.startsWith("video/") == true -> MediaType.VIDEO
            mimeType?.startsWith("image/") == true -> MediaType.IMAGE
            else -> MediaType.VIDEO
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
