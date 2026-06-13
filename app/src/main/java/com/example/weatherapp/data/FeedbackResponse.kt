package com.example.weatherapp.data

// This data class represents what APIdog sends back after we POST
data class FeedbackResponse (
    val message: String? = null, // Optional success message from APIdog
    val success: Boolean? = null // Optional success flag
)