package com.example.profileapp.data.repository

import com.example.profileapp.data.model.*
import com.example.profileapp.data.remote.ApiService
import com.example.profileapp.data.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException

class ApiException(val code: Int, message: String) : Exception(message)

class AuthRepository(private val api: ApiService = RetrofitClient.api) {

    suspend fun register(fullName: String, email: String, password: String): Result<User> =
        safeCall { api.register(RegisterRequest(fullName, email, password)) }.map { it.user }

    suspend fun login(email: String, password: String): Result<AuthResponse> =
        safeCall { api.login(LoginRequest(email, password)) }

    suspend fun getProfile(token: String): Result<User> =
        safeCall { api.me("Bearer $token") }.map { it.user }

    private suspend fun <T> safeCall(block: suspend () -> Response<T>): Result<T> =
        try {
            val response = block()
            val body = response.body()
            if (response.isSuccessful && body != null) Result.success(body)
            else Result.failure(ApiException(response.code(), parseError(response)))
        } catch (e: IOException) {
            Result.failure(ApiException(0, "No se pudo conectar con el servidor"))
        }

    private fun parseError(response: Response<*>): String =
        try {
            Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)?.message
        } catch (e: Exception) {
            null
        } ?: "Error inesperado (${response.code()})"
}
