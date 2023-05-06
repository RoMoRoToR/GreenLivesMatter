package com.example.greenlivesmatter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapViewModel : ViewModel() {
    private val _map = MutableLiveData<GoogleMap>()
    val map: LiveData<GoogleMap> get() = _map

    fun onMapReady(googleMap: GoogleMap) {
        _map.value = googleMap
        val moscow = LatLng(55.751244, 37.618423)
        googleMap.addMarker(MarkerOptions().position(moscow).title("Маркер в Москве"))
    }

}
