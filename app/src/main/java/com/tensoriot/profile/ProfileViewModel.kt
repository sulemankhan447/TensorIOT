package com.tensoriot.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tensoriot.model.WeatherItemModel
import com.tensoriot.model.WeatherMainModel
import com.tensoriot.model.WeatherModel
import com.tensoriot.model.WeatherResponseModel
import com.tensoriot.other.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val repository: ProfileRepository
) : ViewModel() {
    fun filterDataForRecycler(list: ArrayList<WeatherItemModel>?): ArrayList<WeatherMainModel> {
        val resultList = ArrayList<WeatherMainModel>()
        for (i in 0 until 7) {
            val dt = list?.get(i)?.dt
            val minTemp = list?.get(i)?.main?.temp_min
            val maxTemp = list?.get(i)?.main?.temp_max
            val temp = list?.get(i)?.main?.temp
            resultList.add(WeatherMainModel(dt,temp, minTemp, maxTemp))
        }
        return resultList

    }

    var statusLiveData: LiveData<UiState<WeatherResponseModel>> = repository.statusLiveData


    init {
        viewModelScope.launch {
            repository.fetchWeatherInformation()
        }
    }
}