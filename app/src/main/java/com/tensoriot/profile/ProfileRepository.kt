package com.tensoriot.profile

import androidx.lifecycle.MutableLiveData
import com.tensoriot.Constants
import com.tensoriot.api.NetworkInterface
import com.tensoriot.model.WeatherResponseModel
import com.tensoriot.other.UiState
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    val networkInterface: NetworkInterface
) {

    var statusLiveData = MutableLiveData<UiState<WeatherResponseModel>>()


    suspend fun fetchWeatherInformation() {
        kotlin.runCatching {
            networkInterface.fetchWeatherInformation(
                Constants.DEFAULT_LOC,
                Constants.API_KEY
            )
        }.onSuccess { response ->
            if (response.isSuccessful) {
                statusLiveData.value = UiState.Success(response.body())
            } else {
                statusLiveData.value = UiState.Error("Failed to load data for weather")
            }
        }.onFailure {
            statusLiveData.value = UiState.Error(it.message)
        }


    }

}