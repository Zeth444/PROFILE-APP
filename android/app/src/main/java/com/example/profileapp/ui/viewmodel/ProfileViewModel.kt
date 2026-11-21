package com.example.profileapp.ui.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.profileapp.data.local.SessionManager
import com.example.profileapp.data.model.User
import com.example.profileapp.data.repository.ApiException
import com.example.profileapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

/**
 * La imagen de perfil vive ÚNICAMENTE en memoria (estado del ViewModel).
 * - Cámara: Bitmap devuelto por TakePicturePreview (no se crea ningún archivo).
 * - Galería: Uri que solo se lee para mostrarla (no se copia ni se sube).
 */
sealed interface ProfileImage {
    data class FromCamera(val bitmap: Bitmap) : ProfileImage
    data class FromGallery(val uri: Uri) : ProfileImage
}

class ProfileViewModel(private val repo: AuthRepository = AuthRepository()) : ViewModel() {

    var user by mutableStateOf<User?>(SessionManager.user)
        private set
    var image by mutableStateOf<ProfileImage?>(null)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var sessionExpired by mutableStateOf(false)
        private set

    init { loadProfile() }

    private fun loadProfile() {
        val token = SessionManager.token ?: run { sessionExpired = true; return }
        viewModelScope.launch {
            repo.getProfile(token)
                .onSuccess { user = it }
                .onFailure {
                    if ((it as? ApiException)?.code == 401) sessionExpired = true
                    else error = it.message
                }
        }
    }

    fun onPhotoTaken(bitmap: Bitmap) { image = ProfileImage.FromCamera(bitmap) }
    fun onPhotoPicked(uri: Uri) { image = ProfileImage.FromGallery(uri) }

    fun logout() { SessionManager.clear() }
}
