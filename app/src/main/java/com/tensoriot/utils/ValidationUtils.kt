package com.tensoriot.utils

import android.text.TextUtils
import android.util.Patterns


object ValidationUtils {
    fun isValidEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email ?: "").matches()
    }
}