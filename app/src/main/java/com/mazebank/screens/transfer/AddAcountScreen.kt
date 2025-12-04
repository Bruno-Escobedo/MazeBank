package com.mazebank.screens.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mazebank.viewmodels.TransferViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(navController: NavController, transferViewModel: TransferViewModel? = null) {

    // Colores del tema principal (igual que el dashboard)
    val primaryColor = Color(0xFF991B1B)
    val primaryContainer = Color(0xFF7F1D1D)
    val onPrimary = Color.White
    val successColor = Color(0xFF4ADE80)
    val errorColor = Color(0xFFEF4444)

    var nombre by remember { mutableStateOf("") }
    var numeroCuenta by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var shouldNavigate by remember { mutableStateOf(false) }

    // LaunchedEffect para manejar la navegación
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            delay(1500) // Mostrar el mensaje de éxito por 1.5 segundos
            navController.navigate("amount")
            shouldNavigate = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Agregar Nueva Cuenta",
                        color = onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryContainer,
                    titleContentColor = onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryContainer, primaryColor, Color(0xFFDC2626))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Header Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Agregar Contacto",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Registra un nuevo destinatario para transferencias",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }

                // Mostrar mensaje de éxito
                if (successMessage.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(successColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "✅ " + successMessage,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Redirigiendo a transferencia...",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Card principal del formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(0.dp)
                    ) {
                        // Header con icono
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(primaryContainer, primaryColor)
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(Color(0xFFEF4444), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Icon(
                                        Icons.Default.PersonAdd,
                                        contentDescription = "Agregar contacto",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = "Información del Destinatario",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Completa todos los campos requeridos",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        // Formulario
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Campo de nombre
                            Column {
                                Text(
                                    text = "Nombre del Contacto",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = nombre,
                                    onValueChange = { nombre = it },
                                    label = {
                                        Text(
                                            "Nombre completo",
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                        focusedLabelColor = Color.White,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color.White
                                    ),
                                    placeholder = {
                                        Text(
                                            "Ej: Carlos Méndez",
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    },
                                    singleLine = true
                                )
                            }

                            // Campo de número de cuenta
                            Column {
                                Text(
                                    text = "Número de Cuenta",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                OutlinedTextField(
                                    value = formatAccountNumber(numeroCuenta),
                                    onValueChange = {
                                        val value = it.filter { char -> char.isDigit() }
                                        if (value.length <= 16) {
                                            numeroCuenta = value
                                            error = ""
                                        }
                                    },
                                    label = {
                                        Text(
                                            "Número de cuenta bancaria",
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = if (error.isEmpty()) Color.White else errorColor,
                                        unfocusedBorderColor = if (error.isEmpty()) Color.White.copy(alpha = 0.5f) else errorColor,
                                        focusedLabelColor = if (error.isEmpty()) Color.White else errorColor,
                                        unfocusedLabelColor = if (error.isEmpty()) Color.White.copy(alpha = 0.8f) else errorColor,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color.White
                                    ),
                                    placeholder = {
                                        Text(
                                            "1234 5678 9012 3456",
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    },
                                    singleLine = true,
                                    isError = error.isNotEmpty()
                                )

                                // Contador de dígitos
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "16 dígitos requeridos",
                                        color = Color.White.copy(alpha = 0.6f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "${numeroCuenta.length}/16",
                                        color = if (numeroCuenta.length == 16) successColor else Color.White.copy(alpha = 0.6f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Mensaje de error
                                if (error.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(errorColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = error,
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            // Información de ayuda
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF7F1D1D), RoundedCornerShape(12.dp))
                                    .padding(16.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "💡 Información Importante",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "• Verifica que el número de cuenta sea correcto\n" +
                                                "• Las transferencias son inmediatas e irreversibles\n" +
                                                "• Puedes usar este contacto para futuras transferencias",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de continuar
                Button(
                    onClick = {
                        if (nombre.isEmpty()) {
                            error = "El nombre del contacto es requerido"
                            return@Button
                        }
                        if (numeroCuenta.length != 16) {
                            error = "El número de cuenta debe tener exactamente 16 dígitos"
                            return@Button
                        }

                        // GUARDAR EL CONTACTO EN EL VIEWMODEL
                        transferViewModel?.agregarContacto(nombre, numeroCuenta)
                        successMessage = "Contacto guardado exitosamente"

                        // Limpiar el formulario
                        nombre = ""
                        numeroCuenta = ""

                        // Activar la navegación
                        shouldNavigate = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (nombre.isNotEmpty() && numeroCuenta.length == 16) successColor else Color.White.copy(alpha = 0.3f),
                        contentColor = if (nombre.isNotEmpty() && numeroCuenta.length == 16) Color.White else Color.White.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = nombre.isNotEmpty() && numeroCuenta.length == 16
                ) {
                    Text(
                        "Guardar Contacto y Transferir",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Información adicional
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Este contacto se guardará para futuras transferencias",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Función para formatear el número de cuenta con espacios
private fun formatAccountNumber(accountNumber: String): String {
    return accountNumber.chunked(4).joinToString(" ")
}