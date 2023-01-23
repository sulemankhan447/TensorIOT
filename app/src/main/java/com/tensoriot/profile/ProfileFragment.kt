package com.tensoriot.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.tensoriot.Constants
import com.tensoriot.R
import com.tensoriot.databinding.FragmentProfileBinding
import com.tensoriot.model.WeatherMainModel
import com.tensoriot.other.SharedPrefHelper
import com.tensoriot.other.UiState
import com.tensoriot.profile.adapter.WeatherAdapter
import com.tensoriot.utils.ImageUtils
import com.tensoriot.utils.showLongToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : Fragment() {


    @Inject
    lateinit var sharedPrefHelper: SharedPrefHelper

    lateinit var adapter: WeatherAdapter

    lateinit var mBinding: FragmentProfileBinding

    private val profileViewModel: ProfileViewModel by activityViewModels()


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
        setUpListener()
        setUpObserver()
        setUpAdapter()
    }

    private fun setUpListener() {
        mBinding.icLogout.setOnClickListener {
            sharedPrefHelper.clearData()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }

    }

    private fun setUpObserver() {
        profileViewModel.statusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {

                }
                is UiState.Success -> {
                    if (it.data?.list?.isNotEmpty() == true) {
                        val dataToShow = profileViewModel.filterDataForRecycler(it.data.list)
                        adapter.submitList(dataToShow)

                    } else {
                        requireActivity().showLongToast("No weather data for city")
                    }
                }
                is UiState.Error -> {

                }

            }

        }
    }

    private fun setUpAdapter() {
        adapter = WeatherAdapter()
        mBinding.recyclerWeather.adapter = adapter
    }


    private fun setUpUserDetails() {
        val profile = sharedPrefHelper.getString(Constants.PROFILE)
        ImageUtils.loadRemoteImage(mBinding.ivProfile, profile)
        mBinding.tvUsername.text =
            "${getString(R.string.welcome)} ${sharedPrefHelper.getString(Constants.USERNAME)}"
        mBinding.tvShortBio.text =
            sharedPrefHelper.getString(Constants.SHORT_BIO)
    }


}