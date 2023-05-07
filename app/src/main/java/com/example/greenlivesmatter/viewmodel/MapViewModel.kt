package com.example.greenlivesmatter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.TreeMarker
import com.example.greenlivesmatter.data.TreeMarkerRequest
import com.example.greenlivesmatter.network.ApiHelper
import kotlinx.coroutines.launch
import retrofit2.Response

class MapViewModel() : ViewModel() {
    private val apiService = ApiHelper.apiService
    val errorMessage = MutableLiveData<String?>(null)
    fun addTreeMarker(latitude: Double, longitude: Double, isDead: Boolean = false) {
        viewModelScope.launch {
            val newMarker = TreeMarkerRequest(latitude, longitude, isDead)
            // Обработка полученного нового маркера
        }
        viewModelScope.launch {
            try {
                val response = ApiHelper.apiService.addTreeMarker(TreeMarkerRequest(latitude, longitude, false))
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
    private suspend fun sendAddMarkerRequest(
        latitude: Double, longitude: Double, isDead: Boolean
    ): Response<TreeMarker> {
        val treeMarkerRequest = TreeMarkerRequest(latitude, longitude, isDead)
        return apiService.addTreeMarker(treeMarkerRequest)
    }

}

