package com.tensoriot.listener

interface ValidationListener {
    fun onSuccess()
    fun onFailure(msg: Int)
}