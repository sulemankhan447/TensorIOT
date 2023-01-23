package com.tensoriot.model

data class WeatherResponseModel(
    var list: ArrayList<WeatherItemModel>? = ArrayList()
)

data class WeatherItemModel(
    var dt:Long?=0L,
    var main: WeatherMainModel? = null,
    var weather: ArrayList<WeatherModel>? = ArrayList()
)

data class WeatherModel(
    var main: String? = "",
    var description: String? = "",
    var icon: String? = ""
)

data class WeatherMainModel(
    val dt:Long?=0,
    val temp:String?="",
    val temp_min: String? = "",
    var temp_max: String? = ""
)


