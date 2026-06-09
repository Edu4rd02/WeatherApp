package com.example.weatherapp.network

import com.example.weatherapp.data.WeatherResponse
import retrofit2.Response  // Response wraps the result and gives us isSuccessful + error codes
import retrofit2.http.GET  // annotation for HTTP GET requests
import retrofit2.http.Query  // annotation for URL query parameters (?key=value)

// interface = a contract / blueprint
// We write WHAT calls we want, Retrofit generates HOW they actually work
interface WeatherApiService {

    // @GET = make an HTTP GET request to: WEATHER_BASE_URL + "data/2.5/weather"
    // Full URL example: https://api.openweathermap.org/data/2.5/weather?q=London&appid=KEY&units=metric
    @GET("data/2.5/weather")
    // suspend = coroutine function — can pause without blocking the main thread
    // REQUIRED for network calls in modern Android — always use suspend here
    suspend fun getWeather(
        @Query("q")     city: String,    // adds ?q=London to the URL
        @Query("appid") apiKey: String,  // adds &appid=YOUR_KEY
        @Query("units") units: String    // adds &units=metric
    ): Response<WeatherResponse>
    // Response<WeatherResponse> = the result wrapper
    // .isSuccessful = true if HTTP 200-299
    // .body() = the parsed WeatherResponse object (or null if failed)
    // .code() = the HTTP status number (200, 404, 401, etc.)
}