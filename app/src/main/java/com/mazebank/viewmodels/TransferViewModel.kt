package com.mazebank.viewmodels



import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch



data class Contacto(

    val id: Int,

    val nombre: String,

    val numeroCuenta: String

)



class TransferViewModel : ViewModel() {



    // Lista de contactos guardados

    private val _contactos = MutableStateFlow<List<Contacto>>(emptyList())

    val contactos: StateFlow<List<Contacto>> = _contactos



    // Monto actual de transferencia

    private val _montoTransferencia = MutableStateFlow("")

    val montoTransferencia: StateFlow<String> = _montoTransferencia



    // Contacto seleccionado para transferencia

    private val _contactoSeleccionado = MutableStateFlow<Contacto?>(null)

    val contactoSeleccionado: StateFlow<Contacto?> = _contactoSeleccionado



    init {

        // Contactos de ejemplo

        _contactos.value = listOf(

            Contacto(1, "Carlos Méndez", "1234567890123456"),

            Contacto(2, "María González", "2345678901234567"),

            Contacto(3, "Carlos Rodríguez", "3456789012345678")

        )

    }



    fun agregarContacto(nombre: String, numeroCuenta: String) {

        viewModelScope.launch {

            val nuevoContacto = Contacto(

                id = (_contactos.value.maxOfOrNull { it.id } ?: 0) + 1,

                nombre = nombre,

                numeroCuenta = numeroCuenta

            )

            _contactos.value = _contactos.value + nuevoContacto

            // Seleccionar automáticamente el contacto recién creado

            _contactoSeleccionado.value = nuevoContacto

        }

    }



    fun setMontoTransferencia(monto: String) {

        _montoTransferencia.value = monto

    }



    fun getMontoTransferencia(): String {

        return _montoTransferencia.value

    }



    fun setContactoSeleccionado(contacto: Contacto?) {

        _contactoSeleccionado.value = contacto

    }



    fun getContactoSeleccionado(): Contacto? {

        return _contactoSeleccionado.value

    }



    fun limpiarSeleccion() {

        _contactoSeleccionado.value = null

        _montoTransferencia.value = ""

    }

}