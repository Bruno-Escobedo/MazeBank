package com.mazebank.data.repository

import com.mazebank.data.network.RetrofitInstance
import com.mazebank.data.model.LoginResponse
import com.mazebank.data.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {
    private val authApi = RetrofitInstance.authApi

    suspend fun login(correo: String, password: String): Result<LoginResponse> {
        return try {
            println("🚀 [REPOSITORY] Intentando login en: ${RetrofitInstance.BASE_URL}")

            val response = withContext(Dispatchers.IO) {
                authApi.login(
                    com.mazebank.data.model.LoginRequest(correo, password)
                )
            }

            println("📨 [REPOSITORY] Respuesta recibida: $response")

            if (response.error == null && response.usuario != null) {
                println("✅ [REPOSITORY] Login exitoso")
                Result.success(response)
            } else {
                println("❌ [REPOSITORY] Error: ${response.mensaje}")
                Result.failure(Exception(response.mensaje ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            println("💥 [REPOSITORY] Error de conexión: ${e.message}")
            e.printStackTrace()
            Result.failure(Exception("Error de conexión: ${e.message ?: "Verifica tu internet"}"))
        }
    }

    suspend fun register(nombre: String, correo: String, telefono: String, password: String): Result<RegisterResponse> {
        return try {
            println("🚀 [REPOSITORY] Intentando registro en: ${RetrofitInstance.BASE_URL}")

            val response = withContext(Dispatchers.IO) {
                authApi.register(
                    com.mazebank.data.model.RegisterRequest(nombre, correo, telefono, password)
                )
            }

            println("📨 [REPOSITORY] Respuesta recibida: $response")

            if (response.error == null && response.usuario != null) {
                println("✅ [REPOSITORY] Registro exitoso")
                Result.success(response)
            } else {
                println("❌ [REPOSITORY] Error: ${response.mensaje}")
                Result.failure(Exception(response.mensaje ?: "Error en registro"))
            }
        } catch (e: Exception) {
            println("💥 [REPOSITORY] Error de conexión: ${e.message}")
            e.printStackTrace()
            Result.failure(Exception("Error de conexión: ${e.message ?: "Verifica tu internet"}"))
        }
    }
}