package com.example.weatherapp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.annotation.*



class Clouds {
    @SerializedName("all")
    @Expose
    var all: Int? = null
}