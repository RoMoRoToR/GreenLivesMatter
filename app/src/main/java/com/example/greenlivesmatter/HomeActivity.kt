package com.example.greenlivesmatter

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
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme
import com.example.greenlivesmatter.viewmodel.HomeViewModel
import com.example.greenlivesmatter.viewmodel.ProfileViewModel
import com.example.greenlivesmatter.viewmodel.SettingsViewModel



class HomeActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()

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
            bottomBar = {
                val items = listOf(
                    Screen.Map,
                    Screen.Profile,
                    Screen.Settings
                )

                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    items.forEach { screen ->
                        BottomNavigationItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = null) }, // Замените иконку в соответствии с каждым экраном
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
                composable(Screen.Map.route) { MapElement() }
                composable(Screen.Profile.route) { ProfileScreen() } // Замените на ваш компонуемый профиль
                composable(Screen.Settings.route) { SettingsScreen(settingsViewModel) }}
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
            Text(text = "Settings Screen", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Темная тема: ")
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
                Text(text = "Username: ${it.username}", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: ${it.email}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }





    private @Composable
    fun MapElement() {

    }
}



