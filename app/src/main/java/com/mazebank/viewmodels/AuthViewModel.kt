
package com.mazebank.viewmodels



import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.mazebank.data.repository.AuthRepository

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



sealed class AuthState {

    object Idle : AuthState()

    object Loading : AuthState()

    data class Success(val message: String? = null) : AuthState()

    data class Error(val message: String) : AuthState()

}



class AuthViewModel : ViewModel() {



    private val repository = AuthRepository()



    // States para Login

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)

    val loginState: StateFlow<AuthState> = _loginState



    // States para Register

    private val _registerState = MutableStateFlow<AuthState>(AuthState.Idle)

    val registerState: StateFlow<AuthState> = _registerState



    fun login(correo: String, password: String) {

        viewModelScope.launch {

            _loginState.value = AuthState.Loading

            try {

                val result = repository.login(correo, password)

                if (result.isSuccess) {

                    val response = result.getOrNull()

                    // Guardar datos del usuario en UserManager

                    response?.usuario?.let { usuario ->

                        com.mazebank.data.UserManager.setNombreUsuario(usuario.nombre)

                        com.mazebank.data.UserManager.setUserEmail(usuario.correo)

                        com.mazebank.data.UserManager.setUserTelefono(usuario.telefono)

                        com.mazebank.data.UserManager.setUserLoggedIn(true)

                    }

                    _loginState.value = AuthState.Success("Login exitoso")

                } else {

                    val errorMessage = result.exceptionOrNull()?.message ?: "Error desconocido"

                    _loginState.value = AuthState.Error(errorMessage)

                }

            } catch (e: Exception) {

                _loginState.value = AuthState.Error("Error de conexión: ${e.message}")

            }

        }

    }



    fun register(nombre: String, correo: String, telefono: String, password: String, tipoTarjeta: String? = null) {

        viewModelScope.launch {

            _registerState.value = AuthState.Loading

            try {

                val result = repository.register(nombre, correo, telefono, password)

                if (result.isSuccess) {

                    val response = result.getOrNull()

                    // Guardar datos del usuario en UserManager

                    response?.usuario?.let { usuario ->

                        com.mazebank.data.UserManager.setNombreUsuario(usuario.nombre)

                        com.mazebank.data.UserManager.setUserEmail(usuario.correo)

                        com.mazebank.data.UserManager.setUserTelefono(usuario.telefono)

                        tipoTarjeta?.let {

                            com.mazebank.data.UserManager.setTipoTarjetaUsuario(it)

                        }

                        com.mazebank.data.UserManager.setUserLoggedIn(true)

                    }

                    _registerState.value = AuthState.Success("Registro exitoso")

                } else {

                    val errorMessage = result.exceptionOrNull()?.message ?: "Error en el registro"

                    _registerState.value = AuthState.Error(errorMessage)

                }

            } catch (e: Exception) {

                _registerState.value = AuthState.Error("Error de conexión: ${e.message}")

            }

        }

    }



    fun resetLoginState() {

        _loginState.value = AuthState.Idle

    }



    fun resetRegisterState() {

        _registerState.value = AuthState.Idle

    }

}