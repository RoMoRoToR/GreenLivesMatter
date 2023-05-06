package com.example.greenlivesmatter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme
import com.example.greenlivesmatter.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<HomeViewModel>()
            GreenLivesMatterTheme() {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MapElement(viewModel)
                }
            }
        }
    }

    @Composable
    fun MapElement(context: HomeViewModel) {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
        }

        var uiSettings by remember { mutableStateOf(MapUiSettings()) }
        val properties by remember {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL))
        }

        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                properties = properties,
                uiSettings = uiSettings
            )
            Switch(
                checked = uiSettings.zoomControlsEnabled,
                onCheckedChange = {
                    uiSettings = uiSettings.copy(zoomControlsEnabled = it)
                }
            )
        }
    }

}




