package com.example.profileapp.ui.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profileapp.data.local.SessionManager
import com.example.profileapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {

    var loading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            error = "Ingresa tu correo y contraseña"; return
        }
        viewModelScope.launch {
            loading = true; error = null
            repo.login(email.trim(), password)
                .onSuccess { SessionManager.save(it.token, it.user); onSuccess() }
                .onFailure { error = it.message }
            loading = false
        }
    }

    fun register(fullName: String, email: String, password: String, confirm: String, onSuccess: () -> Unit) {
        error = when {
            fullName.isBlank() || email.isBlank() || password.isBlank() -> "Completa todos los campos"
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Correo electrónico inválido"
            password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            password != confirm -> "Las contraseñas no coinciden"
            else -> null
        }
        if (error != null) return

        viewModelScope.launch {
            loading = true
            repo.register(fullName.trim(), email.trim(), password)
                .onSuccess { onSuccess() }
                .onFailure { error = it.message }
            loading = false
        }
    }
}
