package com.example.profileapp.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.profileapp.ui.viewmodel.ProfileImage
import com.example.profileapp.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(onLogout: () -> Unit, vm: ProfileViewModel = viewModel()) {

    // Cámara: devuelve un Bitmap en memoria (sin crear archivos)
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap -> bitmap?.let(vm::onPhotoTaken) }

    // Galería: selector del sistema, no requiere permisos
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> uri?.let(vm::onPhotoPicked) }

    LaunchedEffect(vm.sessionExpired) {
        if (vm.sessionExpired) { vm.logout(); onLogout() }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineMedium)

        ProfileAvatar(vm.image)

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { cameraLauncher.launch(null) }) { Text("Tomar foto") }
            OutlinedButton(onClick = {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }) { Text("Galería") }
        }

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow("Nombre", vm.user?.fullName ?: "—")
                InfoRow("Correo", vm.user?.email ?: "—")
                InfoRow("Miembro desde", vm.user?.createdAt?.take(10) ?: "—")
            }
        }

        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        OutlinedButton(
            onClick = { vm.logout(); onLogout() },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cerrar sesión") }
    }
}

@Composable
private fun ProfileAvatar(image: ProfileImage?) {
    val modifier = Modifier.size(160.dp).clip(CircleShape)
    when (image) {
        is ProfileImage.FromCamera -> Image(
            bitmap = image.bitmap.asImageBitmap(),
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        is ProfileImage.FromGallery -> AsyncImage(
            model = image.uri,
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
        null -> Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant), Alignment.Center) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(80.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
