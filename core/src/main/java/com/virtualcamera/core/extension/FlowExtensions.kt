package com.virtualcamera.core.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResource(): Flow<com.virtualcamera.core.common.Resource<T>> {
    return this
        .map<T, com.virtualcamera.core.common.Resource<T>> {
            com.virtualcamera.core.common.Resource.Success(it)
        }
        .onStart {
            emit(com.virtualcamera.core.common.Resource.Loading)
        }
        .catch { e ->
            emit(
                com.virtualcamera.core.common.Resource.Error(
                    message = e.message ?: "Unknown error",
                    throwable = e
                )
            )
        }
}

fun <T> Flow<T>.onEachResource(
    onLoading: () -> Unit = {},
    onSuccess: (T) -> Unit = {},
    onError: (String) -> Unit = {}
): Flow<com.virtualcamera.core.common.Resource<T>> {
    return this.asResource().onStart { onLoading() }.map { resource ->
        when (resource) {
            is com.virtualcamera.core.common.Resource.Success -> {
                onSuccess(resource.data)
            }
            is com.virtualcamera.core.common.Resource.Error -> {
                onError(resource.message)
            }
            is com.virtualcamera.core.common.Resource.Loading -> {}
        }
        resource
    }
}
