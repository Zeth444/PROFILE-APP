package com.example.profileapp.data.model

data class RegisterRequest(val fullName: String, val email: String, val password: String)
data class LoginRequest(val email: String, val password: String)

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val createdAt: String?
)

data class RegisterResponse(val message: String, val user: User)
data class AuthResponse(val message: String?, val token: String, val user: User)
data class MeResponse(val user: User)
data class ErrorResponse(val message: String?)
