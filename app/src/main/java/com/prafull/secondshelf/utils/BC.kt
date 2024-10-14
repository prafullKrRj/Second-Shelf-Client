package com.prafull.secondshelf.utils

sealed interface BC<out T> {
    data class Success<out T>(val data: T) : BC<T>
    data class Error(val exception: Exception) : BC<Nothing>
    data object Loading : BC<Nothing>
}