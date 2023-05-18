package com.example.greenlivesmatter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.greenlivesmatter.network.ApiHelper
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme
import com.example.greenlivesmatter.viewmodel.HomeViewModel
import com.example.greenlivesmatter.viewmodel.MapViewModel
import com.example.greenlivesmatter.viewmodel.ProfileViewModel
import com.example.greenlivesmatter.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker


class HomeActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val apiService = ApiHelper.apiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreenLivesMatterTheme(darkTheme = settingsViewModel.isDarkTheme.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    HomeScreen(homeViewModel, settingsViewModel)
                }
            }
        }
    }
    @Composable
    fun HomeScreen(viewModel: HomeViewModel, settingsViewModel: SettingsViewModel) {
        val navController = rememberNavController()
        Scaffold(
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    title = { Text(text = "GreenLivesMatter") },
                    actions = {
                        IconButton(onClick = {viewModel.logout(this@HomeActivity)}) {
                            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                        }
                    }
                )
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val items = listOf(Screen.Map, Screen.Settings, Screen.Profile)


                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.route.capitalize()) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Map.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Map.route) { MapScreen() }
                composable(Screen.Profile.route) { ProfileScreen() } // Замените на ваш компонуемый профиль
                composable(Screen.Settings.route) { SettingsScreen(settingsViewModel) }
            }
        }
    }


    @Composable
    fun SettingsScreen(viewModel: SettingsViewModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Dark theme: ")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = viewModel.isDarkTheme.value,
                    onCheckedChange = { viewModel.toggleTheme() }
                )
            }
        }
    }


    @Composable
    fun ProfileScreen() {
        val viewModel by viewModels<ProfileViewModel>()
        val user by viewModel.user.observeAsState()
        val errorMessage by viewModel.errorMessage.observeAsState()

        if (errorMessage != null) {
            Text(text = "Ошибка: $errorMessage", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
        }

        user?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Username: ${it.username}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${it.email}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }


    @Composable
    fun MapScreen() {
        val context = LocalContext.current
        val mapView = rememberMapViewWithLifecycle()
//        val errorMessage by mapViewModel.errorMessage.observeAsState()
        val mapViewModel = remember { MapViewModel(context) }


        val moscow = GeoPoint(55.7522, 37.6156) // координаты Москвы

        val treeMarkers by mapViewModel.treeMarkers.observeAsState(emptyList())

//        if (errorMessage != null) {
//            Text(
//                text = errorMessage!!,
//                color = MaterialTheme.colorScheme.onError,
//                modifier = Modifier.padding(8.dp)
//            )
//        }
        //Вызов fetchTreeMarkers
        LaunchedEffect(key1 = Unit) {
            mapViewModel.fetchTreeMarkers()
        }

        AndroidView(factory = { mapView }) { mapView ->
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.controller.setZoom(14.0)
            mapView.controller.setCenter(moscow)

            // Добавление маркеров на карту
            treeMarkers.forEach { marker ->
                addMarker(
                    context,
                    mapView,
                    GeoPoint(marker.latitude, marker.longitude),
                    mapViewModel,
                    marker.id // передайте идентификатор маркера
                )
            }

//            treeMarkers.forEach { marker ->
//                addMarker(context, mapView, GeoPoint(marker.latitude, marker.longitude))
//            }


            val mapEventsReceiver = object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    return false
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    p?.let { newMarkerPosition ->
                        // Запуск корутины для добавления маркера на сервер
                        lifecycleScope.launch {
                            val markerId = mapViewModel.addTreeMarker(newMarkerPosition.latitude, newMarkerPosition.longitude)
                            markerId?.let {
                                addMarker(context, mapView, newMarkerPosition, mapViewModel, it)
                            }
                        }
                    }
                    return true
                }

            }

            val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
            mapView.overlays.add(mapEventsOverlay)

        }
    }



    fun addMarker(context: Context, mapView: MapView, position: GeoPoint, mapViewModel: MapViewModel, markerId: Int) {
        val marker = Marker(mapView)
        marker.position = position
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "New Tree"
        // Установите слушатель нажатий на маркер
        marker.setOnMarkerClickListener { marker, _ ->
            // Вызовите функции toggleTreeMarkerDeadStatus и deleteTreeMarker здесь
            mapViewModel.toggleTreeMarkerDeadStatus(markerId)
            mapViewModel.deleteTreeMarker(markerId, marker, mapView)

            // Верните true, если вы хотите, чтобы дальнейшие слушатели событий не обрабатывали это событие
            true
        }
        mapView.overlays.add(marker)
        mapView.invalidate()
    }



    @Composable
    fun rememberMapViewWithLifecycle(): MapView {
        val context = LocalContext.current
        val mapView = remember {
            Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", MODE_PRIVATE))
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

}



