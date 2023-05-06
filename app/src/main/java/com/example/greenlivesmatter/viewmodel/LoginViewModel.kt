package com.example.greenlivesmatter.viewmodel
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.data.AuthenticationResult
import com.example.greenlivesmatter.data.LoginRequest
import com.example.greenlivesmatter.network.ApiHelper
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    private val _isAuthenticated = MutableLiveData<AuthenticationResult>()
    val isAuthenticated: LiveData<AuthenticationResult> = _isAuthenticated

    fun authenticate(): Boolean {
        viewModelScope.launch {
            try {
                val response = ApiHelper.apiService.loginUser(LoginRequest(email = email.value, password = password.value))

                if (response.isSuccessful && response.body() != null) {
                    _isAuthenticated.value = AuthenticationResult(success = true)
                } else {
                    val errorMessage = "Неверные учетные данные. Пожалуйста, попробуйте еще раз"
                    _isAuthenticated.value = AuthenticationResult(success = false, errorMessage = errorMessage)
                }
            } catch (e: Exception) {
                // Вывод информации об исключении в лог
                Log.e("Authentication", "Error: ${e.message}", e)

                _isAuthenticated.value = AuthenticationResult(
                    success = false,
                    errorMessage = "Произошла ошибка: ${e.message}. Проверьте подключение к интернету и попробуйте еще раз"
                )
            }
        }
        return true
    }

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }
}
