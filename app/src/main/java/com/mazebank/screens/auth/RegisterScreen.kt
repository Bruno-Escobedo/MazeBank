package com.mazebank.screens.auth

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mazebank.R
import com.mazebank.viewmodels.AuthViewModel
import com.mazebank.data.UserManager
import androidx.compose.foundation.Image
import kotlinx.coroutines.delay

// Data class para el registro
data class RegisterUserData(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val tarjetaSeleccionada: TarjetaGamer? = null
)

// Lista de tarjetas CLÁSICAS - ACTUALIZADA
val tarjetasGamer = listOf(
    TarjetaGamer(
        id = "classic",
        nombre = "Maze Classic",
        limite = "$50,000",
        descripcion = "Tarjeta básica con beneficios esenciales",
        beneficios = listOf("Sin cuota anual", "Cashback 1%", "Seguro básico", "Atención 24/7"),
        color = Color(0xFF666666),
        colorSecundario = Color(0xFF999999),
        textura = "classic"
    ),
    TarjetaGamer(
        id = "bronze",
        nombre = "Maze Bronze",
        limite = "$100,000",
        descripcion = "Ideal para uso frecuente",
        beneficios = listOf("Cashback 2%", "Seguro de viaje", "Asistencia vial", "Descuentos en gasolina"),
        color = Color(0xFFCD7F32),
        colorSecundario = Color(0xFFE0A95C),
        textura = "bronze"
    ),
    TarjetaGamer(
        id = "silver",
        nombre = "Maze Silver",
        limite = "$200,000",
        descripcion = "Más beneficios y mayor límite",
        beneficios = listOf("Cashback 3%", "Lounge aeropuerto", "Seguro ampliado", "Protección de compras"),
        color = Color(0xFFC0C0C0),
        colorSecundario = Color(0xFFE8E8E8),
        textura = "silver"
    ),
    TarjetaGamer(
        id = "gold",
        nombre = "Maze Gold",
        limite = "$500,000",
        descripcion = "Exclusividad y premium",
        beneficios = listOf("Cashback 5%", "Concierge personal", "Seguro premium", "Acceso a eventos"),
        color = Color(0xFFFFD700),
        colorSecundario = Color(0xFFFFEC8B),
        textura = "gold"
    ),
    TarjetaGamer(
        id = "platinum",
        nombre = "Maze Platinum",
        limite = "$1,000,000",
        descripcion = "Máximo nivel de beneficios",
        beneficios = listOf("Cashback 7%", "Acceso VIP", "Seguro completo", "Asesor financiero"),
        color = Color(0xFFE5E4E2),
        colorSecundario = Color(0xFFF0F0F0),
        textura = "platinum"
    ),
    TarjetaGamer(
        id = "diamond",
        nombre = "Maze Diamond",
        limite = "$2,000,000",
        descripcion = "Experiencia bancaria exclusiva",
        beneficios = listOf("Cashback 10%", "Asesor personal", "Beneficios ilimitados", "Servicio prioritario"),
        color = Color(0xFFB9F2FF),
        colorSecundario = Color(0xFFE0F7FF),
        textura = "diamond"
    )
)

data class TarjetaGamer(
    val id: String,
    val nombre: String,
    val limite: String,
    val descripcion: String,
    val beneficios: List<String>,
    val color: Color,
    val colorSecundario: Color,
    val textura: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel  // ✅ AGREGADO: Recibir el ViewModel
) {
    var userData by remember { mutableStateOf(RegisterUserData()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptTerms by remember { mutableStateOf(false) }
    var menuTarjetaExpandido by remember { mutableStateOf(false) }

    val registerState by authViewModel.registerState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Maze Bank Logo",
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Maze Bank",
                color = Color.White,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Crea tu cuenta y selecciona tu tarjeta",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Card del Formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(25.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(28.dp)
                ) {
                    Text(
                        text = "Crear Cuenta + Tarjeta",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Mostrar error si existe
                    if (registerState is com.mazebank.viewmodels.AuthState.Error) {
                        Text(
                            text = "❌ ${(registerState as com.mazebank.viewmodels.AuthState.Error).message}",
                            color = Color.Red,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Campo Nombre
                    OutlinedTextField(
                        value = userData.nombre,
                        onValueChange = { newValue ->
                            userData = userData.copy(nombre = newValue)
                        },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Email
                    OutlinedTextField(
                        value = userData.email,
                        onValueChange = { newValue ->
                            userData = userData.copy(email = newValue)
                        },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Teléfono
                    OutlinedTextField(
                        value = userData.telefono,
                        onValueChange = { newValue ->
                            userData = userData.copy(telefono = newValue)
                        },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Contraseña
                    OutlinedTextField(
                        value = userData.password,
                        onValueChange = { newValue ->
                            userData = userData.copy(password = newValue)
                        },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Confirmar Contraseña
                    OutlinedTextField(
                        value = userData.confirmPassword,
                        onValueChange = { newValue ->
                            userData = userData.copy(confirmPassword = newValue)
                        },
                        label = { Text("Confirmar contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Validar que las contraseñas coincidan
                    if (userData.password.isNotEmpty() && userData.confirmPassword.isNotEmpty() &&
                        userData.password != userData.confirmPassword) {
                        Text(
                            text = "Las contraseñas no coinciden",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // SECCIÓN DE SELECCIÓN DE TARJETA
                    Text(
                        text = "Selecciona tu Tarjeta Maze Bank",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Dropdown para seleccionar tarjeta
                    ExposedDropdownMenuBox(
                        expanded = menuTarjetaExpandido,
                        onExpandedChange = { menuTarjetaExpandido = !menuTarjetaExpandido }
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CreditCard,
                                        contentDescription = "Tarjeta",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = userData.tarjetaSeleccionada?.nombre ?: "Selecciona tu tarjeta",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (userData.tarjetaSeleccionada == null)
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        Icons.Default.ExpandMore,
                                        contentDescription = "Expandir",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            ExposedDropdownMenu(
                                expanded = menuTarjetaExpandido,
                                onDismissRequest = { menuTarjetaExpandido = false }
                            ) {
                                tarjetasGamer.forEach { tarjeta ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .background(
                                                            Brush.verticalGradient(
                                                                colors = listOf(tarjeta.color, tarjeta.colorSecundario)
                                                            ),
                                                            RoundedCornerShape(8.dp)
                                                        )
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Column {
                                                    Text(
                                                        text = tarjeta.nombre,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Text(
                                                        text = "Límite: ${tarjeta.limite}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            userData = userData.copy(tarjetaSeleccionada = tarjeta)
                                            menuTarjetaExpandido = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Mostrar preview de la tarjeta seleccionada
                    val tarjetaSeleccionada = userData.tarjetaSeleccionada
                    if (tarjetaSeleccionada != null) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Preview de tarjeta
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(tarjetaSeleccionada.color, tarjetaSeleccionada.colorSecundario),
                                            start = Offset(0f, 0f),
                                            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                        )
                                    )
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = tarjetaSeleccionada.nombre,
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            Icons.Default.CreditCard,
                                            contentDescription = "Tarjeta",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Text(
                                        text = "•••• •••• •••• 1234",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 16.sp,
                                        letterSpacing = 4.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Límite: ${tarjetaSeleccionada.limite}",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Vence: 12/26",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Mostrar beneficios de la tarjeta seleccionada
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Beneficios:",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        tarjetaSeleccionada.beneficios.take(2).forEach { beneficio ->
                            Text(
                                text = "• $beneficio",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Checkbox Términos y Condiciones
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Checkbox(
                            checked = acceptTerms,
                            onCheckedChange = { newValue ->
                                acceptTerms = newValue
                            },
                            colors = androidx.compose.material3.CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Acepto los términos y condiciones",
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Botón de Registro
                    Button(
                        onClick = {
                            val tarjetaSeleccionada = userData.tarjetaSeleccionada
                            if (userData.password == userData.confirmPassword && acceptTerms && tarjetaSeleccionada != null) {
                                // ✅ LLAMAR AL VIEWMODEL CON LOS DATOS
                                authViewModel.register(
                                    nombre = userData.nombre,
                                    correo = userData.email,
                                    telefono = userData.telefono,
                                    password = userData.password,
                                    tipoTarjeta = tarjetaSeleccionada.id
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        ),
                        enabled = registerState !is com.mazebank.viewmodels.AuthState.Loading &&
                                userData.nombre.isNotEmpty() &&
                                userData.email.isNotEmpty() &&
                                userData.telefono.isNotEmpty() &&
                                userData.password.isNotEmpty() &&
                                userData.confirmPassword.isNotEmpty() &&
                                userData.password == userData.confirmPassword &&
                                acceptTerms &&
                                userData.tarjetaSeleccionada != null
                    ) {
                        if (registerState is com.mazebank.viewmodels.AuthState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "Crear Cuenta + Tarjeta",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Enlace para ir al Login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿Ya tienes una cuenta?",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(
                            onClick = { navController.navigate("login") }
                        ) {
                            Text(
                                text = "Inicia Sesión",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Consulta, transfiere e invierte desde cualquier lugar",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }

    // Manejar navegación cuando registro es exitoso
    LaunchedEffect(key1 = registerState) {
        println("DEBUG: 🔄 RegisterState cambiado: $registerState")

        when (registerState) {
            is com.mazebank.viewmodels.AuthState.Success -> {
                println("DEBUG: ✅ REGISTRO EXITOSO - Navegando al dashboard")

                // Guardar la tarjeta seleccionada y datos del usuario
                val tarjetaSeleccionada = userData.tarjetaSeleccionada
                if (tarjetaSeleccionada != null) {
                    UserManager.setTipoTarjetaUsuario(tarjetaSeleccionada.id)
                    UserManager.setNombreUsuario(userData.nombre)
                    UserManager.setUserEmail(userData.email)
                    UserManager.setUserLoggedIn(true)
                }

                // Pequeño delay para ver los logs
                delay(1000)

                navController.navigate("dashboard") {
                    popUpTo("register") { inclusive = true }
                }
            }
            is com.mazebank.viewmodels.AuthState.Error -> {
                println("DEBUG: ❌ ERROR en registro: ${(registerState as com.mazebank.viewmodels.AuthState.Error).message}")
            }
            else -> {}
        }
    }
}