package com.example.weatherapp.data
// "data" sub-package — all data model files live here

// data class = a Kotlin class designed only to hold data
// Kotlin auto-generates equals(), hashCode(), toString(), and copy() for free
// You never need to write: "if (a.name == b.name && a.main == b.main ...)" — data class handles it
data class WeatherResponse(
    val name: String,             // city name — matches "name" key in JSON
    val main: Main,               // nested object — contains temp and humidity
    val weather: List<Weather>,   // array of weather conditions — we use [0]
    val wind: Wind                // nested object — contains wind speed
)

// Maps to the "main": { } object inside the JSON
data class Main(
    val temp: Double,     // current temperature (Celsius because we pass units=metric)
    val humidity: Int    // humidity as a percentage e.g. 75 = 75%
)

// Maps to each item inside the "weather": [ ] array
data class Weather(
    val description: String  // e.g. "clear sky", "light rain", "overcast clouds"
)

// Maps to the "wind": { } object inside the JSON
data class Wind(
    val speed: Double  // wind speed in meters per second
)