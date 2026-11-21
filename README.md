Profile App – Registro, Login y Perfil

Aplicación móvil Android con registro de usuarios, inicio de sesión y pantalla de perfil con foto (cámara o galería), conectada a un backend propio.

App: Kotlin + Jetpack Compose (arquitectura MVVM + Repository)
Backend: Node.js + Express (API REST con JWT)
Base de datos: SQLite (relacional)
Video de demostración

🎥 Enlace al video: (pendiente, agregar aquí el link)

Estructura del repositorio
├── android/   App móvil (abrir esta carpeta en Android Studio)
├── backend/   API REST + base de datos SQLite
└── README.md


Funcionalidades
Registro de usuario con validaciones (correo válido, contraseña de mínimo 6 caracteres).
Inicio de sesión con token JWT.
Pantalla de perfil con nombre, correo y fecha de registro.
Foto de perfil tomada con la cámara o elegida de la galería.
Cierre de sesión.
1. Backend

Requisitos: Node.js 20 o superior.

cmd
cd backend
npm install
copy .env.example .env
npm start

En macOS/Linux usa cp .env.example .env en lugar de copy. Abre el archivo .env y cambia JWT_SECRET por un texto largo y propio.

El servidor queda en http://localhost:3000. La base de datos (backend/database/app.db) y la tabla users se crean automáticamente al iniciar; no hay que instalar ni configurar nada más. Para comprobar que funciona, abre http://localhost:3000/health y debe responder {"status":"ok"}.

Endpoints
Método	Ruta	Descripción	Requiere token
POST	/api/auth/register	Registra un usuario (bcrypt)	No
POST	/api/auth/login	Devuelve JWT y datos del usuario	No
GET	/api/users/me	Datos del perfil	Sí (Bearer)
Base de datos

Tabla users (definida en backend/database/schema.sql):

Columna	Tipo
id	INTEGER, clave primaria
full_name	TEXT, obligatorio
email	TEXT, obligatorio y único
password_hash	TEXT, obligatorio
created_at	TEXT, fecha de creación
2. App Android
Abrir la carpeta android/ en Android Studio y esperar a que termine el Gradle Sync.
Con el backend encendido, configurar la dirección del servidor en android/app/src/main/java/com/example/profileapp/data/remote/RetrofitClient.kt (constante BASE_URL), según cómo se ejecute la app:
Dónde corre la app	Valor de BASE_URL	Paso adicional
Emulador de Android Studio	http://10.0.2.2:3000/	Ninguno
Celular físico conectado por USB	http://localhost:3000/	Ejecutar adb reverse tcp:3000 tcp:3000 (repetir cada vez que se reconecte el cable)
Celular físico por Wi-Fi	http://IP_DE_TU_PC:3000/	Misma red Wi-Fi y permitir el puerto 3000 en el firewall de Windows
Pulsar Run ▶.

Si adb no está en el PATH, en Windows se ejecuta así:

cmd
"%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe" reverse tcp:3000 tcp:3000
Arquitectura
App (MVVM + Repository)
data/
  model/        Clases de request/response (DTOs)
  remote/       Retrofit: ApiService y RetrofitClient
  local/        SessionManager (token y usuario solo en memoria)
  repository/   AuthRepository (única puerta de entrada a la API)
ui/
  navigation/   NavHost (login → registro → perfil)
  viewmodel/    AuthViewModel y ProfileViewModel (estado de las pantallas)
  screens/      Pantallas en Compose: Login, Register, Profile
Backend (por capas)
routes → controllers → models → SQLite
           (middleware JWT en las rutas protegidas)
Imagen de perfil (no se almacena)

La foto de perfil nunca se guarda ni se envía al servidor:

Cámara: TakePicturePreview devuelve un Bitmap en memoria; no se crea ningún archivo. Por eso la imagen tiene baja resolución (miniatura).
Galería: PickVisualMedia devuelve un Uri que solo se usa para mostrar la imagen con Coil; no se copia ni se sube.
Backend: no existe columna ni endpoint de imagen.
La imagen se descarta al cerrar sesión o al cerrar la app.
