package com.virtualcamera.app.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Context.getFileName(uri: Uri): String {
    val cursor = contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        it.getString(nameIndex) ?: "Unknown"
    } ?: "Unknown"
}

fun Context.getFileSize(uri: Uri): Long {
    val cursor = contentResolver.query(uri, null, null, null, null)
    return cursor?.use {
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        it.moveToFirst()
        it.getLong(sizeIndex)
    } ?: 0L
}

fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}

fun Context.isVideoFile(uri: Uri): Boolean {
    return getMimeType(uri)?.startsWith("video/") == true
}

fun Context.isImageFile(uri: Uri): Boolean {
    return getMimeType(uri)?.startsWith("image/") == true
}

fun Context.isAudioFile(uri: Uri): Boolean {
    return getMimeType(uri)?.startsWith("audio/") == true
}
