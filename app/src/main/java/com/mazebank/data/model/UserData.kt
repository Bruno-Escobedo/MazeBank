package com.mazebank.data.model

// Datos del formulario de usuario para registro
data class UserData(
    val nombre: String,          // Nombre completo del usuario
    val email: String,           // Correo electrónico
    val telefono: String,        // Número de teléfono
    val calle: String,           // Calle de la dirección
    val numero: String,          // Número exterior/interior
    val delegacion: String,      // Delegación o municipio
    val ciudad: String,          // Ciudad
    val tipoTarjeta: String = "Débito" // Tipo de tarjeta que solicita el usuario
)

// Datos del perfil de usuario con información bancaria
data class UserProfile(
    val id: Int,                    // ID único del usuario
    val nombre: String,             // Nombre completo del usuario
    val correo: String,             // Correo electrónico
    val telefono: String,           // Número de teléfono
    val saldo: Double,              // Saldo total disponible
    val cuenta: String,             // Número de cuenta bancaria
    val fechaRegistro: String,      // Fecha de registro en el banco
    val tarjetaAsignada: String     // Tipo de tarjeta asignada al usuario
)

// Datos de tarjetas de crédito/débito del usuario
data class CreditCard(
    val numero: String,             // Número de tarjeta (formato XXXX XXXX XXXX XXXX)
    val tipo: String,               // "Crédito" o "Débito"
    val subtipo: String,            // "Platinum", "Gold", "Infinite", "Clásica"
    val fechaVencimiento: String,   // Fecha de vencimiento (MM/YY)
    val cvv: String,                // Código de seguridad de 3 dígitos
    val saldo: Double,              // Saldo actual de la tarjeta
    var limite: Double? = null,     // Límite de crédito (solo para tarjetas de crédito)
    val nombre: String,             // Nombre de la tarjeta (ej: "Maze Platinum")
    val color: String               // Color de la tarjeta según el tipo
)

// Datos de inversiones del usuario
data class Investment(
    val id: Int,                    // ID único de la inversión
    val nombre: String,             // Nombre del activo/inversión
    val montoInvertido: Double,     // Cantidad de dinero invertido
    val rendimiento: Double,        // Ganancia/pérdida en cantidad monetaria
    val rendimientoPorcentaje: Double, // Ganancia/pérdida en porcentaje
    val fechaInicio: String         // Fecha cuando se inició la inversión
)

// Datos de transacciones del usuario
data class Transaction(
    val id: Int,                    // ID único de la transacción
    val tipo: String,               // Tipo: "transferencia", "pago", "deposito"
    val monto: Double,              // Monto de la transacción
    val destinatario: String?,      // Persona/empresa que recibió el dinero
    val fecha: String,              // Fecha cuando se realizó la transacción
    val descripcion: String         // Descripción detallada de la transacción
)

// Datos de contactos frecuentes para transferencias
data class Contacto(
    val nombre: String,             // Nombre del contacto
    val cuenta: String,             // Número de cuenta del contacto
    val banco: String = "Maze Bank" // Banco al que pertenece la cuenta
)

// Datos completos para mostrar en el dashboard
data class DashboardData(
    val usuario: UserProfile,       // Información del perfil del usuario
    val tarjetas: List<CreditCard>, // Lista de tarjetas del usuario
    val inversiones: List<Investment>, // Lista de inversiones del usuario
    val transacciones: List<Transaction>, // Historial de transacciones recientes
    val usuariosFrecuentes: List<Contacto> // Lista de contactos para transferencias
)