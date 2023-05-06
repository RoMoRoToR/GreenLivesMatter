package com.example.greenlivesmatter

sealed class Screen(val route: String) {
    object Map : Screen("map")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

