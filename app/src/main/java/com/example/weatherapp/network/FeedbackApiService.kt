package com.example.weatherapp.network
import com.example.weatherapp.data.FeedbackRequest
import com.example.weatherapp.data.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body // @Body = sends this object as the JSON request body
import retrofit2.http.POST

interface FeedbackApiService {
    @POST(value = "feedback")
    suspend fun submitFeedback(
        @Body request: FeedbackRequest
    ): Response<FeedbackResponse>
}