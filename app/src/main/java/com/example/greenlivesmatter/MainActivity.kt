package com.example.greenlivesmatter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greenlivesmatter.ui.theme.GreenLivesMatterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreenLivesMatterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }



    @Composable
    fun RegistrationScreen(navController: NavHostController) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Registration", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    performRegistration(username, password)
                    navController.navigate("login") // переход на экран входа
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Register")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    performRegistration(username, password)
                    navController.navigate("login") // переход на экран входа
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Login")
            }
        }

    }

    private fun performRegistration(username: String, password: String) {
        // Perform registration logic here
    }

    @Composable
    fun LoginScreen() {
        // код для экрана входа
    }

    @Composable
    fun MyApp() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "registration") {
            composable("registration") { RegistrationScreen(navController) }
            composable("login") { LoginScreen() }
        }
    }
}

