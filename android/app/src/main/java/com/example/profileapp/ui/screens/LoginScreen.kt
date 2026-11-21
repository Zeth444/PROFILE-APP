package com.example.profileapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profileapp.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    justRegistered: Boolean,
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit,
    vm: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)

        if (justRegistered) {
            Text("¡Registro exitoso! Ahora inicia sesión.", color = MaterialTheme.colorScheme.primary)
        }

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Correo electrónico") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = { vm.login(email, password, onLoginSuccess) },
            enabled = !vm.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (vm.loading) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            else Text("Entrar")
        }

        TextButton(onClick = onGoRegister) { Text("¿No tienes cuenta? Regístrate") }
    }
}
