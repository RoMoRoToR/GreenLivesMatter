package com.example.greenlivesmatter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector) {
    object Map : Screen("map", Icons.Default.Home)
    object Settings : Screen("settings", Icons.Default.Settings)
    object Profile : Screen("profile", Icons.Default.AccountCircle)
}

