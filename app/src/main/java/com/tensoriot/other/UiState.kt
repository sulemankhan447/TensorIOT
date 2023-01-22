package com.tensoriot.other

sealed class UiState {
    class Loading : UiState()
    class Success : UiState()
    class Error(val msg:String?) : UiState()
}
