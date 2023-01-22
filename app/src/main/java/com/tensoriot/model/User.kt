package com.tensoriot.model

data class User(
    var profile: String? = null,
    var email: String? = "",
    var password: String? = "",
    var confirmPassword: String? = "",
    var username: String? = "",
    var shortBio: String? = ""
)
