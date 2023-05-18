package com.example.greenlivesmatter.viewmodel
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenlivesmatter.network.ApiHelper.apiService
import com.example.greenlivesmatter.network.TokenManager
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _logoutResult = MutableLiveData<Result<Unit>>()
    fun logout(context: Context) {
        val token = TokenManager.getToken(context)
        if (token != null) {
            viewModelScope.launch {
                try {
                    val response = apiService.logoutUser(token)
                    if (response.isSuccessful) {
                        _logoutResult.value = Result.success(Unit)
                    } else {
                        _logoutResult.value = Result.failure(Exception("Logout failed"))
                    }
                } catch (e: Exception) {
                    _logoutResult.value = Result.failure(e)
                }
            }
        } else {
            // Handle the case when the token is null
        }
    }
}