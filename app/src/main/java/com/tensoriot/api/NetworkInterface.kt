package com.tensoriot.api

import com.tensoriot.model.WeatherResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkInterface {

    //    @GET("forecast?q=Mumbai&appid=2a9f8ca6f644719f03963276974be387")
    @GET("forecast")
    suspend fun fetchWeatherInformation(
        @Query("q") city: String,
        @Query("appid") appId: String
    ): Response<WeatherResponseModel>
}