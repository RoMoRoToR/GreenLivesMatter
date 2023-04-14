package com.example.greenlivesmatter

import RegistrationViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme
import com.example.greenlivesmatter.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel = viewModel<LoginViewModel>()
            val registrationViewModel = viewModel<RegistrationViewModel>()
            val currentScreen = remember {mutableStateOf("login")}
            GreenLivesMatterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (currentScreen.value == "login") {
                        LoginScreen(loginViewModel) {
                            // Обработка успешной аутентификации, переход к другой активности, например
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        Text(
                            text = "Don't have an account? Sign up",
                            modifier = Modifier.clickable {
                                currentScreen.value = "registration"
                            }
                        )
                    } else {
                        RegistrationScreen(registrationViewModel) {
                            // Обработка успешной регистрации, переход к другой активности, например
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        Text(
                            text = "Already have an account? Log in",
                            modifier = Modifier.clickable {
                                currentScreen.value = "login"
                            }
                        )
                    }
                }
            }
        }
    }
}

