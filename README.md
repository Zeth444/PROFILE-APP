# Profile App – Registro, Login y Perfil

App Android (Kotlin + Jetpack Compose) + API REST (Node.js/Express) + SQLite (base relacional).

```
profile-app/
├── android/   -> App móvil (abrir esta carpeta en Android Studio)
└── backend/   -> API REST + SQLite (esquema en database/schema.sql)
```

## 1. Backend (requiere Node.js 18 o superior)
```bash
cd backend
npm install
cp .env.example .env        # (en Windows: copy .env.example .env) y cambia JWT_SECRET
npm start                   # http://localhost:3000
```
La base de datos SQLite (`database/app.db`) y la tabla `users` se crean solas al iniciar el servidor.
No hay que instalar ni configurar nada más. Para probar: abre http://localhost:3000/health

| Método | Ruta               | Descripción                         | Auth   |
|--------|--------------------|-------------------------------------|--------|
| POST   | /api/auth/register | Registra usuario (bcrypt)           | No     |
| POST   | /api/auth/login    | Devuelve JWT + datos del usuario    | No     |
| GET    | /api/users/me      | Datos del perfil                    | Bearer |

## 2. App Android
1. Abrir la carpeta `android/` en Android Studio y esperar el Gradle Sync.
2. Emulador: la URL base ya es `http://10.0.2.2:3000/`.
   Celular físico: cambiar `BASE_URL` en `data/remote/RetrofitClient.kt` por la IP de tu PC.
3. Run ▶.

## Arquitectura (MVVM + Repository, por capas)
```
data/
  model/       Clases de request/response (DTOs)
  remote/      Retrofit: ApiService + RetrofitClient
  local/       SessionManager (token en memoria)
  repository/  AuthRepository (única puerta de entrada a la API)
ui/
  navigation/  NavHost (login -> register -> profile)
  viewmodel/   AuthViewModel, ProfileViewModel (estado de pantalla)
  screens/     Composables: Login, Register, Profile
```
Backend: `routes -> controllers -> models -> SQLite` (+ middleware JWT).

## Imagen de perfil (NO se almacena)
- Cámara: `TakePicturePreview` devuelve un `Bitmap` en memoria; no se crea archivo.
- Galería: `PickVisualMedia` devuelve un `Uri` que solo se muestra con Coil; no se copia ni se sube.
- No existe columna ni endpoint de imagen en el backend. Al cerrar sesión/app, la imagen se descarta.

## Subir a GitHub
```bash
git init && git add . && git commit -m "App de perfil: Android + backend"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/TU_REPO.git
git push -u origin main
```
