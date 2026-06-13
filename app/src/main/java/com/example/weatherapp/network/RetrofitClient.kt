package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// object = singleton — only one RetrofitClient exists in the entire app
object RetrofitClient {

    // "by lazy" = create this only when it is first accessed
    // If weatherApiService is never used, it's never created — saves memory
    // Once created, the same instance is reused every time — no rebuilding
    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.WEATHER_BASE_URL)
            // baseUrl = "https://api.openweathermap.org/"
            // Retrofit prepends this to every @GET path in WeatherApiService
            .addConverterFactory(GsonConverterFactory.create())
            // GsonConverterFactory = automatic JSON → data class conversion
            // When the API returns JSON, Gson maps it to WeatherResponse fields
            .build()
            .create(WeatherApiService::class.java)
        // .create() = Retrofit generates the real HTTP implementation of our interface
        // WeatherApiService::class.java = tells Retrofit which interface to implement
    }
    val feedbackApiService: FeedbackApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstants.FEEDBACK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedbackApiService::class.java)
    }
}