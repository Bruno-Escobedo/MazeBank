package com.mazebank.screens.investments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mazebank.R
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingScreen(navController: NavController, operacion: String = "comprar") {
    var criptoSeleccionada by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var operacionExitosa by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Colores del tema principal (igual que el dashboard)
    val primaryColor = Color(0xFF991B1B) // Rojo oscuro
    val primaryContainer = Color(0xFF7F1D1D) // Rojo más oscuro
    val onPrimary = Color.White

    val criptomonedas = listOf(
        "BTC" to "Bitcoin",
        "ETH" to "Ethereum",
        "ADA" to "Cardano",
        "SOL" to "Solana",
        "XRP" to "Ripple",
        "DOT" to "Polkadot",
        "DOGE" to "Dogecoin",
        "AVAX" to "Avalanche",
        "LINK" to "Chainlink",
        "MATIC" to "Polygon"
    )

    val precios = mapOf(
        "BTC" to 65432.21,
        "ETH" to 3456.78,
        "ADA" to 0.45,
        "SOL" to 143.21,
        "XRP" to 0.56,
        "DOT" to 6.78,
        "DOGE" to 0.12,
        "AVAX" to 34.56,
        "LINK" to 14.32,
        "MATIC" to 0.67
    )
    val precioActual = precios[criptoSeleccionada]

    val valorTotal = if (cantidad.isNotEmpty() && precioActual != null) {
        val cantidadValue = cantidad.toDoubleOrNull() ?: 0.0
        precioActual * cantidadValue
    } else {
        0.0
    }

    // Función simple para formatear
    fun formatNumber(number: Double): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    if (operacionExitosa) {
        LaunchedEffect(Unit) {
            delay(2000)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (operacion == "comprar") "Comprar Criptomonedas" else "Vender Criptomonedas",
                            color = onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryContainer, primaryColor, Color(0xFFDC2626))
                    )
                )
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (operacionExitosa) {
                AlertDialog(
                    onDismissRequest = { },
                    confirmButton = { },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Éxito",
                                tint = Color(0xFF4ADE80),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "¡Operación realizada con éxito!",
                                color = Color(0xFF4ADE80),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    text = {
                        Text("Tu ${if (operacion == "comprar") "compra" else "venta"} de criptomonedas ha sido procesada correctamente. Redirigiendo a la página de inversiones...")
                    },
                    containerColor = Color.White
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = primaryColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = if (operacion == "comprar") "Comprar Criptomonedas" else "Vender Criptomonedas",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (operacion == "comprar")
                                "Selecciona la criptomoneda que deseas comprar"
                            else
                                "Selecciona la criptomoneda que deseas vender",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                TextField(
                                    value = criptoSeleccionada.let { simbolo ->
                                        criptomonedas.find { it.first == simbolo }?.let {
                                            val precio = precios[it.first] ?: 0.0
                                            "${it.second} (${it.first}) - $${formatNumber(precio)}"
                                        } ?: "Selecciona una criptomoneda"
                                    },
                                    onValueChange = {},
                                    label = { Text("Criptomoneda", color = Color.White.copy(alpha = 0.8f)) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.White,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                        focusedLabelColor = Color.White,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        cursorColor = Color.White
                                    ),
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                    },
                                    singleLine = true
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    criptomonedas.forEach { (simbolo, nombre) ->
                                        DropdownMenuItem(
                                            text = {
                                                val precio = precios[simbolo] ?: 0.0
                                                Text("$nombre ($simbolo) - $${formatNumber(precio)}")
                                            },
                                            onClick = {
                                                criptoSeleccionada = simbolo
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = cantidad,
                                onValueChange = {
                                    if (it.isEmpty() || it.toDoubleOrNull() != null) {
                                        cantidad = it
                                    }
                                },
                                label = { Text("Cantidad", color = Color.White.copy(alpha = 0.8f)) },
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
                                placeholder = { Text("0.00", color = Color.White.copy(alpha = 0.5f)) },
                                singleLine = true
                            )

                            if (criptoSeleccionada.isNotEmpty() && cantidad.isNotEmpty() && cantidad.toDoubleOrNull() != null) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF991B1B)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Resumen de la operación:",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 14.sp
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Precio unitario:", color = Color.White)
                                            Text(
                                                "$${formatNumber(precioActual ?: 0.0)}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Cantidad:", color = Color.White)
                                            Text(
                                                "${cantidad} $criptoSeleccionada",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "Valor total:",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                "$${formatNumber(valorTotal)}",
                                                color = if (operacion == "comprar") Color(0xFF4ADE80) else Color(0xFFEF4444),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = { operacionExitosa = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (operacion == "comprar") Color(0xFF4ADE80) else Color(0xFFEF4444),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                enabled = criptoSeleccionada.isNotEmpty() && cantidad.isNotEmpty() && cantidad.toDoubleOrNull() != null
                            ) {
                                Text(
                                    if (operacion == "comprar") "Confirmar Compra" else "Confirmar Venta",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}