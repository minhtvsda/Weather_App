package com.example.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
//    PART_OF_URL = "weather?lat=21.0227346&lon=105.7957639&appid=5c9b4d0f4485694060f33ffc9ad5151d&units=metric"


    @GET("weather?appid=5c9b4d0f4485694060f33ffc9ad5151d&units=metric")
    fun getWeatherWithLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<OpenWeatherMap?>?

    @GET("weather?appid=5c9b4d0f4485694060f33ffc9ad5151d&units=metric")
    fun getWeatherWithCityName(
        @Query("q") name: String
    ): Call<OpenWeatherMap?>?
}