package com.example.greenlivesmatter.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.User
import com.example.greenlivesmatter.network.ApiHelper
import com.example.greenlivesmatter.network.TokenManager
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        loadUserData()
    }

    // Загрузка данных пользователя с сервера
    fun loadUserData() {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(getApplication())
                if (token != null) {
                    Log.d("ProfileViewModel", "Sending request to API")
                    val response = ApiHelper.apiService.getUser(token)

                    if (response.isSuccessful && response.body() != null) {
                        _user.value = response.body()!!
                        Log.d("ProfileViewModel", "User data loaded successfully")
                    } else {
                        // Обработка ошибки получения данных пользователя
                        val errorMessage = "Ошибка при получении данных пользователя: ${response.errorBody()}"
                        _errorMessage.value = errorMessage
                        Log.e("ProfileViewModel", errorMessage)
                    }
                } else {
                    val errorMessage = "Отсутствует токен аутентификации"
                    _errorMessage.value = errorMessage
                    Log.e("ProfileViewModel", errorMessage)
                }
            } catch (e: Exception) {
                // Обработка исключения при получении данных пользователя
                val errorMessage = "Exception: ${e.message}"
                _errorMessage.value = errorMessage
                Log.e("ProfileViewModel", errorMessage, e)
            }
        }
    }

    // Обновление данных пользователя
    fun updateUserData(updatedUser: User) {
        viewModelScope.launch {
            // Здесь код для обновления данных пользователя на сервере
            // _user.value = updatedUser
        }
    }
}
