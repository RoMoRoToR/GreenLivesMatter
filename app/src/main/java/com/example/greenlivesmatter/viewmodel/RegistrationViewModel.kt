package com.example.greenlivesmatter.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.RegisterRequest
import com.example.greenlivesmatter.data.RegistrationResult
import com.example.greenlivesmatter.network.ApiHelper
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    private val _isRegistered = MutableLiveData<RegistrationResult>()
    val isRegistered: LiveData<RegistrationResult> = _isRegistered
    fun register(): Boolean {
        viewModelScope.launch {
            try {
//                val user = User(name.value, email.value, password.value)
                val response = ApiHelper.apiService.registerUser(RegisterRequest(email = email.value, name = name.value, password = password.value))

                if (response.isSuccessful) {
                    // Регистрация прошла успешно
                    _isRegistered.value = RegistrationResult(success = true)
                } else {
                    // Обработка неудачной регистрации
                    val errorMessage = when (response.code()) {
                        409 -> "Email или имя пользователя уже зарегистрированы"
                        else -> "Не удалось зарегистрироваться. Пожалуйста, попробуйте еще раз"
                    }
                    _isRegistered.value = RegistrationResult(success = false, errorMessage = errorMessage)
                }
            } catch (e: Exception) {
                // Обработка ошибок, например, нет доступа к интернету
                _isRegistered.value = RegistrationResult(success = false, errorMessage = "Произошла ошибка. Проверьте подключение к интернету и попробуйте еще раз")
            }
        }
        return true
    }

    fun onNameChanged(newName: String) {
        name.value = newName
    }

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }
}
