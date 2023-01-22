package com.tensoriot.auth

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.tensoriot.R
import com.tensoriot.listener.ValidationListener
import com.tensoriot.model.LoginRequestModel
import com.tensoriot.model.User
import com.tensoriot.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {


    var loading = ObservableBoolean()

    var validationListener: ValidationListener? = null


    fun loginUser(request:LoginRequestModel){
        repository.loginUser(request)
    }
    fun registerUser(request: User) {
        repository.registerUser(request)
    }

    fun hideLoader() {
        loading.set(false)

    }

    fun showLoader() {
        loading.set(true)
    }

    fun validateLoginUser(request: LoginRequestModel) {
        if (request.email?.isEmpty() == true) {
            validationListener?.onFailure(R.string.email_error)
            return
        }
        if (ValidationUtils.isValidEmail(request.email).not()) {
            validationListener?.onFailure(R.string.valid_email_error)
            return
        }
        if (request.password?.isEmpty() == true) {
            validationListener?.onFailure(R.string.password_error)
            return
        }
        if ((request.password?.length ?: 0) < 6) {
            validationListener?.onFailure(R.string.password_length_error)
            return
        }

        validationListener?.onSuccess()
    }

    fun validateRegisterUser(request: User) {
        if (request.email?.isEmpty() == true) {
            validationListener?.onFailure(R.string.email_error)
            return
        }
        if (ValidationUtils.isValidEmail(request.email).not()) {
            validationListener?.onFailure(R.string.valid_email_error)
            return
        }
        if (request.password?.isEmpty() == true) {
            validationListener?.onFailure(R.string.password_error)
            return
        }
        if ((request.password?.length ?: 0) < 6) {
            validationListener?.onFailure(R.string.password_length_error)
            return
        }
        if (request.confirmPassword?.isEmpty() == true) {
            validationListener?.onFailure(R.string.confirm_password_error)
            return
        }
        if (request.password.equals(request.confirmPassword, true).not()) {
            validationListener?.onFailure(R.string.password_dont_match)
            return
        }
        if (request.username?.isEmpty() == true) {
            validationListener?.onFailure(R.string.username_error)
            return
        }
        if (request.shortBio?.isEmpty() == true) {
            validationListener?.onFailure(R.string.bio_error)
            return
        }
        validationListener?.onSuccess()

    }

}