package com.example.greenlivesmatter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    // Загрузка данных пользователя с сервера
    fun loadUserData() {
        viewModelScope.launch {
            // Здесь ваш код для загрузки данных пользователя с сервера
            // _user.value = loadedUser
        }
    }

    // Обновление данных пользователя
    fun updateUserData(updatedUser: User) {
        viewModelScope.launch {
            // Здесь ваш код для обновления данных пользователя на сервере
            // _user.value = updatedUser
        }
    }
}
