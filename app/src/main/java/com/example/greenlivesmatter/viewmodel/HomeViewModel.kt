package com.example.greenlivesmatter.viewmodel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val welcomeMessage = mutableStateOf("Welcome to the Home Screen!")

}