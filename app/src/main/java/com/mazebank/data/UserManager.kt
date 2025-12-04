package com.mazebank.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserManager {
    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        prefs = context.getSharedPreferences("maze_bank_prefs", Context.MODE_PRIVATE)
    }

    // Guardar datos del usuario
    fun setUserLoggedIn(isLoggedIn: Boolean) {
        prefs.edit { putBoolean("is_logged_in", isLoggedIn) }
    }

    fun setNombreUsuario(nombre: String) {
        prefs.edit { putString("nombre_usuario", nombre) }
    }

    fun setUserEmail(email: String) {
        prefs.edit { putString("user_email", email) }
    }

    fun setUserTelefono(telefono: String) {
        prefs.edit { putString("user_telefono", telefono) }
    }

    fun setTipoTarjetaUsuario(tipoTarjeta: String) {
        prefs.edit { putString("tipo_tarjeta", tipoTarjeta) }
    }

    // Obtener datos del usuario
    fun isUserLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun getNombreUsuario(): String? {
        return prefs.getString("nombre_usuario", null)
    }

    fun getUserEmail(): String? {
        return prefs.getString("user_email", null)
    }

    fun getUserTelefono(): String? {
        return prefs.getString("user_telefono", null)
    }

    fun getTipoTarjetaUsuario(): String? {
        return prefs.getString("tipo_tarjeta", null)
    }

    // Validar si el usuario está autenticado
    fun isAuthenticated(): Boolean {
        return isUserLoggedIn() && getNombreUsuario() != null
    }

    // Obtener datos completos del usuario
    data class UserData(
        val nombre: String?,
        val email: String?,
        val telefono: String?,
        val tipoTarjeta: String?
    )

    fun getUserData(): UserData {
        return UserData(
            nombre = getNombreUsuario(),
            email = getUserEmail(),
            telefono = getUserTelefono(),
            tipoTarjeta = getTipoTarjetaUsuario()
        )
    }

    // Cerrar sesión
    fun logout() {
        prefs.edit {
            putBoolean("is_logged_in", false)
            remove("nombre_usuario")
            remove("user_email")
            remove("user_telefono")
            remove("tipo_tarjeta")
        }
    }
}