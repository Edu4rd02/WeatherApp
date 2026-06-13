package com.example.weatherapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.FeedbackRequest
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.network.AppConstants
import com.example.weatherapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var currentCity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.init(context = this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetWeather.setOnClickListener {
            val city = binding.etCity.text.toString().trim()
            if (city.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            currentCity = city
            fetchWeather(city)
        }
        binding.btnSubmitFeedback.setOnClickListener {
            if (currentCity.isEmpty()){
                Toast.makeText(this, "Please fetch weather for a city first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.etComment.text.toString().trim()
            if (comment.isEmpty()){
                Toast.makeText(this, "Please leave a comment!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            submitFeedback(currentCity, rating, comment)
        }
    }

    private fun fetchWeather(city: String) {
        binding.progressBar.isGone = false
        binding.btnGetWeather.isEnabled = false
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
            finally {
                binding.progressBar.isGone = true
                binding.btnGetWeather.isEnabled = true
            }
        }
    }

    private fun submitFeedback(city: String, rating: Int, comment: String){
        binding.progressBarFeedback.isGone = false
        binding.btnSubmitFeedback.isEnabled = false
        lifecycleScope.launch{
            try {
                val response = withContext(Dispatchers.IO){
                    RetrofitClient.feedbackApiService.submitFeedback(
                        request = FeedbackRequest(
                            city = city,
                            rating = rating,
                            comment = comment
                        )
                    )
                }
                if (response.isSuccessful){
                    binding.etComment.text.clear()
                    binding.ratingBar.rating = 3f
                    binding.tvFeedbackResult.text = "Feedback submitted successfully!"
                } else {
                    binding.tvFeedbackResult.text = "Failed to submit feedback: ${response.message()}"
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            finally {
                binding.progressBarFeedback.isGone = true
                binding.btnSubmitFeedback.isEnabled = true
            }
        }
    }
}