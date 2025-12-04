package com.mazebank.screens.loan

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mazebank.R
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanScreen(navController: NavController) {
    var monto by remember { mutableStateOf(10000f) }
    var plazo by remember { mutableStateOf(12f) }
    var motivo by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Colores del tema principal (igual que el dashboard)
    val primaryColor = Color(0xFF991B1B) // Rojo oscuro
    val primaryContainer = Color(0xFF7F1D1D) // Rojo más oscuro
    val onPrimary = Color.White

    val tasaInteres = 12.5f
    val tasaMensual = tasaInteres / 12 / 100
    val pagoMensual = (monto * tasaMensual) / (1 - Math.pow((1 + tasaMensual).toDouble(), -plazo.toDouble())).toFloat()
    val totalPagar = pagoMensual * plazo
    val interesesTotales = totalPagar - monto

    val motivos = listOf(
        "Mejoras en vivienda",
        "Compra de vehículo",
        "Educación",
        "Consolidación de deudas",
        "Inversión en negocio",
        "Emergencia médica",
        "Vacaciones",
        "Boda",
        "Otro"
    )

    // Función para formatear números
    fun formatNumber(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
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
                        Text("Solicitar Préstamo", color = onPrimary, fontWeight = FontWeight.Bold)
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
                .padding(16.dp)
        ) {
            // CARD PRINCIPAL
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
                    // HEADER
                    Column(
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Text(
                            text = "Préstamo Personal",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Completa el formulario para solicitar tu préstamo",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        // MONTO DEL PRÉSTAMO
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF991B1B)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Monto del préstamo",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.CurrencyExchange,
                                            contentDescription = "Monto",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "$${formatNumber(monto.toInt())}",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Slider(
                                    value = monto,
                                    onValueChange = { monto = it },
                                    valueRange = 5000f..100000f,
                                    steps = 94,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = androidx.compose.material3.SliderDefaults.colors(
                                        thumbColor = Color.White,
                                        activeTrackColor = Color(0xFFEF4444),
                                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                    )
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("$5,000", color = Color.White.copy(alpha = 0.7f))
                                    Text("$100,000", color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }

                        // PLAZO DEL PRÉSTAMO
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF991B1B)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Plazo (meses)",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.CalendarToday,
                                            contentDescription = "Plazo",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${plazo.toInt()} meses",
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Slider(
                                    value = plazo,
                                    onValueChange = { plazo = it },
                                    valueRange = 12f..60f,
                                    steps = 3,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = androidx.compose.material3.SliderDefaults.colors(
                                        thumbColor = Color.White,
                                        activeTrackColor = Color(0xFFEF4444),
                                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                                    )
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("12 meses", color = Color.White.copy(alpha = 0.7f))
                                    Text("60 meses", color = Color.White.copy(alpha = 0.7f))
                                }
                            }
                        }

                        // MOTIVO DEL PRÉSTAMO
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF991B1B)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Motivo del préstamo",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded }
                                ) {
                                    TextField(
                                        value = motivo,
                                        onValueChange = {},
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(),
                                        label = { Text("Selecciona un motivo", color = Color.White.copy(alpha = 0.8f)) },
                                        readOnly = true,
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color.White,
                                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                            focusedLabelColor = Color.White,
                                            unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            cursorColor = Color.White
                                        )
                                    )

                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        motivos.forEach { item ->
                                            DropdownMenuItem(
                                                text = { Text(item) },
                                                onClick = {
                                                    motivo = item
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Divider(color = Color.White.copy(alpha = 0.3f))

                        // RESUMEN DEL PRÉSTAMO
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF991B1B)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Percent,
                                        contentDescription = "Resumen",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "Resumen del préstamo",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // INFORMACIÓN DEL PRÉSTAMO
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                "Tasa de interés anual",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "$tasaInteres%",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                "Pago mensual",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "$${"%.2f".format(pagoMensual)}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                "Total a pagar",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "$${"%.2f".format(totalPagar)}",
                                                color = Color.White,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                "Intereses totales",
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                "$${"%.2f".format(interesesTotales)}",
                                                color = Color(0xFFEF4444),
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }
                                }

                                // DESGLOSE ADICIONAL
                                Spacer(modifier = Modifier.height(16.dp))
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF7F1D1D)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Detalles adicionales",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "Monto solicitado:",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                "$${formatNumber(monto.toInt())}",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                "Plazo seleccionado:",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                "${plazo.toInt()} meses",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // BOTÓN DE SOLICITUD ACTUALIZADO
                        Button(
                            onClick = {
                                val montoFormateado = formatNumber(monto.toInt())
                                val plazoFormateado = plazo.toInt().toString()
                                navController.navigate("loan/success/$montoFormateado/$plazoFormateado")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF4444),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Solicitar Préstamo",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // INFORMACIÓN ADICIONAL
                        Text(
                            text = "• Tu solicitud será revisada en un plazo de 24-48 horas\n" +
                                    "• Tasa de interés fija durante todo el plazo\n" +
                                    "• Sin penalizaciones por pago anticipado",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}