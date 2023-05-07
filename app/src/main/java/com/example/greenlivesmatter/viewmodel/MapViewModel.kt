package com.example.greenlivesmatter.viewmodel

import android.content.Context
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.TreeMarker
import com.example.greenlivesmatter.data.TreeMarkerRequest
import com.example.greenlivesmatter.data.TreeMarkerResponse
import com.example.greenlivesmatter.network.ApiHelper
import kotlinx.coroutines.launch
import retrofit2.Response

class MapViewModel(context: Context) : ViewModel() {
    private val apiService = ApiHelper.apiService
    val errorMessage = MutableLiveData<String?>(null)

    fun addTreeMarker(latitude: Double, longitude: Double, isDead: Boolean = false) {
        viewModelScope.launch {
            try {
                val response = sendAddMarkerRequest(latitude, longitude, false)
                if (response.isSuccessful) {
                    // Маркер успешно добавлен на сервер
                    val markerData = response.body()
                    // Вы можете использовать markerData для дальнейших действий, например, для отображения данных маркера
                } else {
                    // Обработка ошибок сервера
                    errorMessage.postValue("Ошибка сервера: ${response.code()}")
                }
            } catch (e: Exception) {
                // Обработка ошибок сети
                errorMessage.postValue("Ошибка сети: ${e.message}")
            }
        }
    }

    private val _treeMarkers = MutableLiveData<List<TreeMarkerResponse>>()
    val treeMarkers: LiveData<List<TreeMarkerResponse>>
        get() = _treeMarkers

    suspend fun fetchTreeMarkers() {
        try {
            val response = apiService.getTreeMarkers()
            if (response.isSuccessful) {
                val markers = response.body()
                markers?.let {
                    _treeMarkers.value = it.toTreeMarkerResponseList()
                }
            } else {
                Log.e(TAG, "Error fetching tree markers: response not successful, code: ${response.code()}, message: ${response.message()}")
                // Выводите тело ответа сервера для анализа
                Log.e(TAG, "Server response body: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching tree markers: ${e.message}", e)
        }
    }


    fun List<TreeMarker>.toTreeMarkerResponseList(): List<TreeMarkerResponse> {
        return this.map { marker ->
            TreeMarkerResponse(
                id = marker.id,
                latitude = marker.latitude,
                longitude = marker.longitude,
                is_dead = marker.is_dead
            )
        }
    }




    private suspend fun sendAddMarkerRequest(
        latitude: Double, longitude: Double, isDead: Boolean
    ): Response<TreeMarker> {
        val treeMarkerRequest = TreeMarkerRequest(latitude, longitude, isDead)
        return apiService.addTreeMarker(treeMarkerRequest)
    }
}

