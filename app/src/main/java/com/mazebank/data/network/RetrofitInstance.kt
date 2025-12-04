package com.mazebank.data.network

import android.util.Log
import com.mazebank.data.model.LoginRequest
import com.mazebank.data.model.LoginResponse
import com.mazebank.data.model.RegisterRequest
import com.mazebank.data.model.RegisterResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

object RetrofitInstance {
    // ✅ URL DE NGROK
    const val BASE_URL = "https://noninverted-unpresumably-cecilia.ngrok-free.dev/"

    // TAG para logging
    private const val TAG = "RETROFIT_INSTANCE"

    init {
        Log.d(TAG, "🚀 RetrofitInstance inicializado")
        Log.d(TAG, "🌐 URL Base: $BASE_URL")
    }

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, "📡 $message")
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApiService by lazy {
        Log.d(TAG, "✅ AuthApiService creado")
        retrofit.create(AuthApiService::class.java)
    }
}
