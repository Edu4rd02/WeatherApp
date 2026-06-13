package com.example.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okio.IOException
import java.io.File
import java.util.concurrent.TimeUnit

class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var lastException: IOException? = null
        repeat(times = maxRetries){
            try{
                return chain.proceed(chain.request())
            } catch (e: IOException) {
                lastException = e
            }
        }
        throw lastException ?: IOException("Request failed after $maxRetries retries")
    }
}

object RetrofitClient{
    private lateinit var appContext: Context
    fun init(context: Context){
        appContext = context.applicationContext
    }
    val weatherApiService: WeatherApiService by lazy {
        val cacheDir = File(appContext.cacheDir, "http_cache")
        val cache = Cache(cacheDir,5L*1024*1024) // 5 MB
        val okHttpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor { chain ->
                chain.proceed(chain.request()).newBuilder()
                    .header("Cache-Control", "public, max-age=300") // max-age: How much time the cache lives
                    .build()
            }
            .addInterceptor(RetryInterceptor(maxRetries = 3))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(AppConstants.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    val feedbackApiService : FeedbackApiService by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor(maxRetries = 3))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(AppConstants.FEEDBACK_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedbackApiService::class.java)

    }
}