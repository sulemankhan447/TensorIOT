package com.tensoriot.auth.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tensoriot.Constants
import com.tensoriot.R
import com.tensoriot.auth.AuthViewModel
import com.tensoriot.auth.FirebaseRepository
import com.tensoriot.auth.SQLRepository
import com.tensoriot.databinding.FragmentLoginBinding
import com.tensoriot.listener.ValidationListener
import com.tensoriot.model.LoginRequestModel
import com.tensoriot.other.SharedPrefHelper
import com.tensoriot.other.UiState
import com.tensoriot.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), ValidationListener {

    lateinit var mBinding: FragmentLoginBinding

    private val authViewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.validationListener = this
        mBinding.viewModel = authViewModel
        if(sharedPrefHelper.getBoolean(Constants.IS_LOGIN)){
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }
        else{
            setUpListener()
            setUpObserver()
        }
    }

    private fun setUpObserver() {
        val liveDataToObserve = when (authViewModel.repository) {
            is FirebaseRepository -> {
                (authViewModel.repository as FirebaseRepository).statusLiveData
            }
            else -> {
                (authViewModel.repository as SQLRepository).statusLiveData
            }
        }
        liveDataToObserve.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    authViewModel.showLoader()
                }
                is UiState.Success -> {
                    authViewModel.hideLoader()
                    findNavController().navigate(R.id.action_loginFragment_to_profileFragment)

                }
                is UiState.Error -> {
                    authViewModel.hideLoader()
                    requireActivity().showLongToast(
                        if (!TextUtils.isEmpty(it.msg)) it.msg
                            ?: "" else getString(R.string.signin_failed)
                    )
                }

            }
        }

    }

    private fun setUpListener() {
        mBinding.btnLogin.setOnClickListener {
            authViewModel.validateLoginUser(getLoginRequestModel())

        }
        mBinding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun getLoginRequestModel(): LoginRequestModel {
        val request = LoginRequestModel()
        request.email = mBinding.edtEmail.text.toString().trim()
        request.password = mBinding.edtPassword.text.toString().trim()
        return request
    }

    override fun onSuccess() {
        authViewModel.loginUser(getLoginRequestModel())
    }

    override fun onFailure(msg: Int) {
        showToast(getString(msg))

    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }


}