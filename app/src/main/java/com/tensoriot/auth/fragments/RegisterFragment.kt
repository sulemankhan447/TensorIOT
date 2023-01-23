package com.tensoriot.auth.fragments

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.tensoriot.Constants
import com.tensoriot.R
import com.tensoriot.auth.AuthViewModel
import com.tensoriot.auth.FirebaseRepository
import com.tensoriot.auth.SQLRepository
import com.tensoriot.databinding.FragmentRegisterBinding
import com.tensoriot.listener.ValidationListener
import com.tensoriot.model.User
import com.tensoriot.other.SharedPrefHelper
import com.tensoriot.other.UiState
import com.tensoriot.utils.FileUtils
import com.tensoriot.utils.ImageUtils
import com.tensoriot.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RegisterFragment : Fragment(), ValidationListener {

    private lateinit var mBinding: FragmentRegisterBinding

    private val authViewModel: AuthViewModel by activityViewModels()

    private var imageUri: Uri? = null

    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper


    @Inject
    lateinit var mAuth: FirebaseAuth

    var isProfileSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.validationListener = this
        mBinding.viewModel = authViewModel
        setUpImageUri()
        setUpListener()
        setUpObserver()
    }

    private fun setUpImageUri() {
        FileUtils.createImageUri(requireActivity())?.let {
            imageUri = it
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

                    findNavController().navigate(R.id.action_registerFragment_to_profileFragment)

                }
                is UiState.Error -> {
                    authViewModel.hideLoader()
                    requireActivity().showLongToast(
                        if (!TextUtils.isEmpty(it.errorMessage)) it.errorMessage
                            ?: "" else getString(R.string.signup_failed)
                    )
                }

            }
        }


    }

    private fun showToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_LONG).show()
    }


    private fun setUpListener() {
        mBinding.btnSignUp.setOnClickListener {
            authViewModel.validateRegisterUser(getUserModel())
        }
        mBinding.ivProfile.setOnClickListener {
            if (isProfileSet.not()) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCamera()
                } else {
                    cameraPermissionContract.launch(android.Manifest.permission.CAMERA)
                }
            }
        }
    }

    private fun startCamera() {
        takePictureContract.launch(imageUri)

    }

    private fun getUserModel(): User {
        return User(
            profile = imageUri.toString(),
            email = mBinding.edtEmail.text.toString().trim(),
            password = mBinding.edtPassword.text.toString().trim(),
            confirmPassword = mBinding.edtConfirmPassword.text.toString().trim(),
            username = mBinding.edtUsername.text.toString().trim(),
            shortBio = mBinding.edtShortBio.text.toString().trim()
        )
    }

    override fun onSuccess() {
        authViewModel.registerUser(getUserModel())
    }

    override fun onFailure(msg: Int) {
        showToast(getString(msg))
    }

    private val cameraPermissionContract =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if(isGranted){
                startCamera()
            }
            else{
                showToast("Please provide permission to access camera for display picture")
            }
        }

    private val takePictureContract =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            isProfileSet = true
            ImageUtils.loadLocalImage(mBinding.ivProfile, imageUri)
        }

}