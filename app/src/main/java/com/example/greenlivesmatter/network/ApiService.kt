package com.example.greenlivesmatter.network

import com.example.greenlivesmatter.data.LoginRequest
import com.example.greenlivesmatter.data.LoginResponse
import com.example.greenlivesmatter.data.RegisterRequest
import com.example.greenlivesmatter.data.RegisterResponse
import com.example.greenlivesmatter.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("user")
    suspend fun getUser(@Header("x-access-token") token: String): Response<User>
}
