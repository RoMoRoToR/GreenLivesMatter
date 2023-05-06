package com.example.greenlivesmatter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme
import com.example.greenlivesmatter.viewmodel.LoginViewModel



class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel = viewModel<LoginViewModel>()

            GreenLivesMatterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginScreen(loginViewModel) {
                        // Отображение сообщения об успешной аутентификации
                        Toast.makeText(this, "Successfully logged in!", Toast.LENGTH_SHORT).show()
                        // Обработка успешной аутентификации, переход к другой активности
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                }
            }
        }
    }
}

    @Composable
    fun LoginScreen(viewModel: LoginViewModel, onAuthenticated: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.password.value,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.authenticate()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            viewModel.isAuthenticated.observeAsState(initial = null).value?.let { authenticationResult ->
                if (authenticationResult.success) {
                    onAuthenticated()
                } else {
                    // Обработка неудачной аутентификации, например, отображение сообщения об ошибке
                    Toast.makeText(
                        LocalContext.current,
                        authenticationResult.errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
