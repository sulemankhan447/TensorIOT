package com.tensoriot.other

sealed class UiState<T>(val data: T? = null, val errorMessage: String? = null) {
    class Loading<T> : UiState<T>()
    class Success<T>(data: T? = null) : UiState<T>(data = data)
    class Error<T>(errorMessage: String?) : UiState<T>(errorMessage = errorMessage)
}
