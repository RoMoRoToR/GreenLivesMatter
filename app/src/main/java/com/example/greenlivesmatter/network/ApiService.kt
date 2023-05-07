package com.example.greenlivesmatter.network

import com.example.greenlivesmatter.data.LoginRequest
import com.example.greenlivesmatter.data.LoginResponse
import com.example.greenlivesmatter.data.RegisterRequest
import com.example.greenlivesmatter.data.RegisterResponse
import com.example.greenlivesmatter.data.TreeMarker
import com.example.greenlivesmatter.data.TreeMarkerRequest
import com.example.greenlivesmatter.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @GET("user")
    suspend fun getUser(@Header("x-access-token") token: String): Response<User>

    @GET("tree_markers")
    suspend fun getTreeMarkers(): Response<List<TreeMarker>>

    @POST("tree_markers")
    suspend fun addTreeMarker(@Body treeMarker: TreeMarkerRequest): Response<TreeMarker>

    @DELETE("tree_markers/{marker_id}")
    suspend fun deleteTreeMarker(@Path("marker_id") markerId: Int): Response<String>
}
