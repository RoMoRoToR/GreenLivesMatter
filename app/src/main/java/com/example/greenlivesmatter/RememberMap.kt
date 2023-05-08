package com.example.greenlivesmatter

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", ComponentActivity.MODE_PRIVATE))
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
        }
    }

    // Следим за жизненным циклом
    DisposableEffect(key1 = mapView) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
        }
    }

    return mapView
}