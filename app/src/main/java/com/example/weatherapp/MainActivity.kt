package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.network.AppConstants
import com.example.weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            fetchWeather(city)
        }
    }

    private fun fetchWeather(city: String) {

        // LifecycleScope <- coroutine scope tied to this activity
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO){
                    RetrofitClient.weatherApiService.getWeather(
                        city = city,
                        apiKey = AppConstants.API_KEY,
                        units = AppConstants.UNITS
                    )
                }
                if (response.isSuccessful){
                    val weather = response.body()
                    if (weather != null) {
                        binding.tvCity.text = "City: ${weather.name}"
                        binding.tvTemperature.text = "Temperature: ${weather.main.temp} °C"
                        binding.tvDescription.text = "Weather: ${weather.weather[0].description}"
                        binding.tvWindSpeed.text = "Wind Speed: ${weather.wind.speed} m/s"
                        binding.tvHumidity.text = "Humidity: ${weather.main.humidity}%"
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "City not found. Check the name and try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}