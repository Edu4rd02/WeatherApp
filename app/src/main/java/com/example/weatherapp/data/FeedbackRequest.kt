package com.example.weatherapp.data

// This data class represents the JSON body we SEND to the APIdog
data class FeedbackRequest (
    val city: String,
    val rating: Int,
    val comment: String
)