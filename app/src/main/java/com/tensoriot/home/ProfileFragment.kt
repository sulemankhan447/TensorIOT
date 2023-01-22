package com.tensoriot.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.tensoriot.Constants
import com.tensoriot.R
import com.tensoriot.databinding.FragmentProfileBinding
import com.tensoriot.other.SharedPrefHelper
import com.tensoriot.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {


    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserDetails()
        setUpProfileImage()
    }

    private fun setUpUserDetails() {
        val profile = sharedPrefHelper.getString(Constants.PROFILE)
        ImageUtils.loadRemoteImage(mBinding.ivProfile, profile)
        mBinding.tvUsername.text = "${getString(R.string.welcome)} ${sharedPrefHelper.getString(Constants.USERNAME)}"
    }

    private fun setUpProfileImage() {

    }

}