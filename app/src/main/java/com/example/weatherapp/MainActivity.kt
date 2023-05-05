package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    var lat = 0.0
    var lon = 0.0


    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener{
            startActivity(Intent(this, WeatherActivity::class.java))
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(p0: Location) {
                lat = p0.latitude
                lon = p0.longitude
                Log.e("lat", lat.toString())
                Log.e("lon", lon.toString())
                getWeatherData(lat, lon)
            }
            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }   else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 50f, locationListener)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && permissions.isNotEmpty() && ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 50f, locationListener)

    }
    fun getWeatherData(lat: Double, lon: Double){
        val weatherApi = RetrofitWeather.client.create(WeatherApi::class.java)
        val call = weatherApi.getWeatherWithLocation(lat, lon)
        call?.enqueue(object : Callback<OpenWeatherMap?>{    //retrieve data asynchronously(background)
            override fun onResponse(
                call: Call<OpenWeatherMap?>,
                response: Response<OpenWeatherMap?>
            ) {
                val body = response.body()
                if (body == null){
                    Toast.makeText(this@MainActivity, "Failed to get body information", Toast.LENGTH_LONG).show()
                    return
                }
                binding.textViewCity.text = body.name+" , "+ body.sys?.country
                binding.textViewTemp.text = ""+body.main?.temp +" °C"
                binding.textViewWeatherCondition.text = body.weather!!.get(0).description
                binding.textViewHumidity.text = " : "+body.main?.humidity+ " %"
                binding.textViewMaxTemp.text = " : " + body.main?.tempMax+ " °C"
                binding.textViewMinTemp.text = " : " + body.main?.tempMin+ " °C"
                binding.textViewPressure.text = " : " + body.main!!.pressure + " hPa"
                binding.textViewWind.text = " : " + body.wind?.speed + "mph"

                val iconCode = body.weather!![0].icon
                Picasso.get().load("http://openweathermap.org/img/wn/$iconCode@2x.png")
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imageView)

            }

            override fun onFailure(call: Call<OpenWeatherMap?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to get information", Toast.LENGTH_LONG).show()
            }

        })



    }

}