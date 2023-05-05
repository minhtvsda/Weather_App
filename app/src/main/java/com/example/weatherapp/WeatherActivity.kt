package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.search.setOnClickListener {
            val cityName = binding.editTextCityName.text.toString()
            getWeatherData(cityName)
            binding.editTextCityName.setText("")
        }

    }
    fun getWeatherData(cityName:String){
        val weatherApi = RetrofitWeather.client.create(WeatherApi::class.java)
        val call = weatherApi.getWeatherWithCityName(cityName)
        call?.enqueue(object : Callback<OpenWeatherMap?> {    //retrieve data asynchronously(background)
            override fun onResponse(
                call: Call<OpenWeatherMap?>,
                response: Response<OpenWeatherMap?>
            ) {
                if (!response.isSuccessful){
                    Toast.makeText(this@WeatherActivity, "City not found! Please try again!", Toast.LENGTH_LONG).show()
                    return
                }

                val body = response.body() ?: return

                binding.textViewCity.text = body.name+" , "+ body.sys?.country
                binding.textViewTemp.text = ""+body.main?.temp +" °C"
                binding.textViewWeatherCondition.text = body.weather!!.get(0).description
                binding.textViewHumidity.text = " : "+body.main?.humidity+ " %"
                binding.textViewMaxTemp.text = " : " + body.main?.tempMax+ " °C"
                binding.textViewMinTemp.text = " : " + body.main?.tempMin+ " °C"
                binding.textViewPressure.text = " : " + body.main!!.pressure +  " hPa"
                binding.textViewWind.text = " : " + body.wind?.speed + "mph"

                val iconCode = body.weather!![0].icon
                Picasso.get().load("http://openweathermap.org/img/wn/$iconCode@2x.png")
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imageViewWeather)

            }

            override fun onFailure(call: Call<OpenWeatherMap?>, t: Throwable) {
                Toast.makeText(this@WeatherActivity, "Failed to get information", Toast.LENGTH_LONG).show()
            }

        })

    }

}
