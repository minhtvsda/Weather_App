package com.example.weatherapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWeather {
    companion object {
        const val WEBSITE_RECEIVE_DATA = "https://api.openweathermap.org/data/2.5/"

        var retrofit: Retrofit? = null
        val client : Retrofit
        get() {
            return  retrofit ?: Retrofit.Builder()
                .baseUrl(WEBSITE_RECEIVE_DATA)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }


}