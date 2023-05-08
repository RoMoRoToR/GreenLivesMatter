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
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.Response

class MapViewModel(context: Context) : ViewModel() {
    private val apiService = ApiHelper.apiService
    val errorMessage = MutableLiveData<String?>(null)

    suspend fun addTreeMarker(latitude: Double, longitude: Double, isDead: Boolean = false): Int? {
        return try {
            val response = sendAddMarkerRequest(latitude, longitude, false)
            if (response.isSuccessful) {
                // Маркер успешно добавлен на сервер
                val markerData = response.body()
                markerData?.id // Возвращает идентификатор нового маркера
            } else {
                // Обработка ошибок сервера
                errorMessage.postValue("Ошибка сервера: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Обработка ошибок сети
            errorMessage.postValue("Ошибка сети: ${e.message}")
            null
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


    fun toggleTreeMarkerDeadStatus(markerId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.toggleTreeMarkerDeadStatus(markerId)
                if (response.isSuccessful) {
                    // Маркер успешно обновлен на сервере
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

    fun deleteTreeMarker(markerId: Int, marker: Marker, mapView: MapView) {
        viewModelScope.launch {
            try {
                val response = sendDeleteMarkerRequest(markerId)
                if (response.isSuccessful) {
                    // Удаляем маркер с карты
                    marker.closeInfoWindow()
                    mapView.overlays.remove(marker)
                    mapView.invalidate()

                    // Маркер успешно удален с сервера
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

    private suspend fun sendDeleteMarkerRequest(markerId: Int): Response<Unit> {
        return apiService.deleteTreeMarker(markerId)
    }



    private suspend fun sendAddMarkerRequest(
        latitude: Double, longitude: Double, isDead: Boolean
    ): Response<TreeMarker> {
        val treeMarkerRequest = TreeMarkerRequest(latitude, longitude, isDead)
        return apiService.addTreeMarker(treeMarkerRequest)
    }

}

