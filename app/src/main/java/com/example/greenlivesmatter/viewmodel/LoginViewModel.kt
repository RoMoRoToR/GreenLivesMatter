package com.example.greenlivesmatter.viewmodel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    fun authenticate(): Boolean {
        // Здесь должна быть реализация проверки email и пароля, например, через API-запрос или другой способ
        // В этом примере мы считаем, что аутентификация успешна, если email и пароль непустые
        return email.value.isNotEmpty() && password.value.isNotEmpty()
    }

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }
}