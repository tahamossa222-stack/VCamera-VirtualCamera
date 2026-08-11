package com.virtualcamera.app.presentation.screen.media

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.virtualcamera.app.domain.model.MediaSource
import com.virtualcamera.app.domain.repository.MediaSourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MediaPreviewUiState(
    val isLoading: Boolean = true,
    val mediaSource: MediaSource? = null,
    val error: String? = null
)

@HiltViewModel
class MediaPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MediaSourceRepository
) : ViewModel() {

    private val mediaId: Long = savedStateHandle.get<Long>("mediaId") ?: 0L

    private val _uiState = MutableStateFlow(MediaPreviewUiState())
    val uiState: StateFlow<MediaPreviewUiState> = _uiState.asStateFlow()

    init {
        loadMediaSource()
    }

    private fun loadMediaSource() {
        viewModelScope.launch {
            val source = repository.getMediaSourceById(mediaId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                mediaSource = source
            )
        }
    }

    fun selectAsSource() {
        viewModelScope.launch {
            _uiState.value.mediaSource?.let { source ->
                repository.updateLastUsedAt(source.id)
            }
        }
    }
}
