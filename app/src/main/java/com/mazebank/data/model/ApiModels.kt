package com.mazebank.data.model

data class LoginRequest(
    val correo: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val correo: String,
    val telefono: String,
    val password: String
)

data class LoginResponse(
    val mensaje: String,
    val usuario: Usuario?,
    val error: String?
)

data class RegisterResponse(
    val mensaje: String,
    val usuario: Usuario?,
    val error: String?
)

data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    val telefono: String
)