package com.mazebank.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mazebank.R
import com.mazebank.data.UserManager
import kotlin.math.max
import kotlin.random.Random

// Data class para representar una criptomoneda
data class CryptoAsset(
    val symbol: String,
    val name: String,
    val amount: Double,
    val currentPrice: Double,
    val performance: Double,
    val color: Color,
    val priceHistory: List<Double> = emptyList()
)

// Data class para tarjeta de crédito (local, no necesita import)
data class TarjetaCredito(
    val nombre: String,
    val tipo: String,
    val color: Color,
    val colorSecundario: Color
)

// Data class para transacciones
data class TransactionItem(
    val nombre: String,
    val monto: String,
    val fecha: String
)

// Función para generar datos de historial de precios realistas
fun generatePriceHistory(currentPrice: Double, volatility: Double = 0.1): List<Double> {
    val random = Random(System.currentTimeMillis())
    val history = mutableListOf<Double>()
    var price = currentPrice * (1 - volatility / 2)

    repeat(10) {
        val change = (random.nextDouble() - 0.4) * volatility
        price *= (1 + change)
        price = max(price, currentPrice * 0.1)
        history.add(price)
    }

    history[history.size - 1] = currentPrice
    return history
}

// Gráfica simplificada para el dashboard
@Composable
fun DashboardInvestmentChart(
    cryptoAssets: List<CryptoAsset>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF991B1B), RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        if (cryptoAssets.isNotEmpty() && cryptoAssets.any { it.priceHistory.isNotEmpty() }) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawDashboardChart(cryptoAssets, size)
            }

            val totalPerformance = cryptoAssets.map { it.performance }.average()
            Text(
                text = "${if (totalPerformance >= 0) "+" else ""}${"%.1f".format(totalPerformance)}%",
                color = if (totalPerformance >= 0) Color(0xFF4ADE80) else Color(0xFFEF4444),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 4.dp, bottom = 2.dp)
            )
        } else {
            Text(
                text = "📈 Gráfica de Rendimiento",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun DrawScope.drawDashboardChart(
    cryptoAssets: List<CryptoAsset>,
    size: androidx.compose.ui.geometry.Size
) {
    val assetsWithHistory = cryptoAssets.filter { it.priceHistory.isNotEmpty() }
    if (assetsWithHistory.isEmpty()) return

    val mainAssets = assetsWithHistory.take(3)
    val allPrices = mainAssets.flatMap { it.priceHistory }
    val maxPrice = allPrices.maxOrNull() ?: 1.0
    val minPrice = allPrices.minOrNull() ?: 0.0
    val priceRange = maxPrice - minPrice

    val chartWidth = size.width
    val chartHeight = size.height * 0.8f
    val verticalPadding = size.height * 0.1f

    mainAssets.forEach { crypto ->
        val path = Path()
        val points = crypto.priceHistory

        if (points.size >= 2) {
            val firstX = 0f
            val firstY = chartHeight - (((points.first() - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding
            path.moveTo(firstX, firstY)

            points.forEachIndexed { index, price ->
                if (index > 0) {
                    val x = (index.toFloat() / (points.size - 1)) * chartWidth
                    val y = chartHeight - (((price - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = crypto.color,
                style = Stroke(width = 2f)
            )
        }
    }

    val avgPrice = allPrices.average()
    val avgY = chartHeight - (((avgPrice - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding

    drawLine(
        color = Color.White.copy(alpha = 0.2f),
        start = Offset(0f, avgY),
        end = Offset(chartWidth, avgY),
        strokeWidth = 1f
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    navController: NavController,
    userName: String
) {
    var showProfileMenu by remember { mutableStateOf(false) }

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
                Text("Maze Bank", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF7F1D1D),
            titleContentColor = Color.White
        ),
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(userName.split(" ")[0], color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Text(
                        "Cerrar sesión",
                        color = Color(0xFFFCA5A5),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .clickable {
                                UserManager.logout()
                                navController.navigate("login") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            }
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { showProfileMenu = true },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF991B1B), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Perfil",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun DashboardBottomBar() {
    var showAccountMenu by remember { mutableStateOf(false) }
    var showHelpMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF7F1D1D))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { }
        ) {
            Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White)
            Text("Inicio", color = Color.White, fontSize = 10.sp)
        }

        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { showAccountMenu = true }
            ) {
                Icon(Icons.Default.CreditCard, contentDescription = "Cuenta", tint = Color.White)
                Text("Cuenta", color = Color.White, fontSize = 10.sp)
            }
        }

        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { showHelpMenu = true }
            ) {
                Icon(Icons.Default.Help, contentDescription = "Ayuda", tint = Color.White)
                Text("Ayuda", color = Color.White, fontSize = 10.sp)
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { }
        ) {
            Icon(Icons.Default.Description, contentDescription = "Políticas", tint = Color.White)
            Text("Políticas", color = Color.White, fontSize = 10.sp)
        }
    }
}

@Composable
fun WelcomeSection(
    userName: String,
    userEmail: String
) {
    Column {
        Text(
            text = "Hola, $userName",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userEmail,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CreditCardSection() {
    // Colores basados en el tipo de tarjeta del usuario
    val tipoTarjeta = UserManager.getTipoTarjetaUsuario() ?: "standard"
    val (cardColor, secondaryColor, cardName) = when (tipoTarjeta) {
        "gold" -> Triple(Color(0xFFD97706), Color(0xFFFBBF24), "Maze Gold")
        "platinum" -> Triple(Color(0xFF6B7280), Color(0xFF9CA3AF), "Maze Platinum")
        else -> Triple(Color(0xFF1E3A8A), Color(0xFF3B82F6), "Maze Clásica")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(cardColor, secondaryColor)
                    )
                )
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Tarjeta de Crédito",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = cardName,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        Icons.Default.CreditCard,
                        contentDescription = "Tarjeta",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "5432 1098 7654 3210",
                    color = Color.White,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Fecha de vencimiento",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "12/28",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column {
                        Text(
                            text = "CVV",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "***",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Column {
                        Text(
                            text = "Tipo",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Crédito",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransferSection(
    navController: NavController,
    availableBalance: String = "$45,750.00"
) {
    Spacer(modifier = Modifier.height(24.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF991B1B)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Transferencia",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Envía dinero a tus contactos",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    text = "Saldo disponible para transferir",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Text(
                    text = availableBalance,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("transfer") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Enviar",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun InvestmentsSection(
    navController: NavController,
    cryptoPortfolio: List<CryptoAsset>,
    totalInvestment: Double,
    totalPerformance: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF991B1B)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Inversiones en Cripto",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Rendimiento de tu portafolio",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Valor total",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${"%.2f".format(totalInvestment)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = "Rendimiento",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${if (totalPerformance >= 0) "+" else ""}${"%.1f".format(totalPerformance)}%",
                        color = if (totalPerformance >= 0) Color(0xFF4ADE80) else Color(0xFFEF4444),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                cryptoPortfolio.take(2).forEach { crypto ->
                    Column {
                        Text(
                            text = crypto.symbol,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${"%.2f".format(crypto.amount)}",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Column {
                    Text(
                        text = "Total",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${cryptoPortfolio.size} activos",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DashboardInvestmentChart(
                cryptoAssets = cryptoPortfolio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("investments") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = "Invertir",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Detalles", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun FrequentUsersSection(
    frequentUsers: List<Pair<String, String>>,
    showFrequentUsers: Boolean,
    onToggleShow: () -> Unit,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF991B1B)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Usuarios Frecuentes",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Contactos a los que envías dinero frecuentemente",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                val usersToShow = if (showFrequentUsers) frequentUsers else frequentUsers.take(1)

                usersToShow.forEach { (nombre, iniciales) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = iniciales,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = nombre, color = Color.White)
                        }
                        OutlinedButton(
                            onClick = { navController.navigate("transfer") },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text("Transferir")
                        }
                    }
                    if (nombre != usersToShow.last().first) {
                        Divider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onToggleShow,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    if (showFrequentUsers) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (showFrequentUsers) "Mostrar menos" else "Mostrar más"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (showFrequentUsers) "Mostrar menos" else "Mostrar más")
            }
        }
    }
}

@Composable
fun TransactionHistorySection(
    transactionHistory: List<TransactionItem>,
    showTransactionHistory: Boolean,
    onToggleShow: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF991B1B)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Historial de Transferencias",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Últimas transferencias realizadas",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                val transactionsToShow = if (showTransactionHistory) transactionHistory else transactionHistory.take(1)

                transactionsToShow.forEachIndexed { index, transaction ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = transaction.nombre,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = transaction.fecha,
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = transaction.monto,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    if (index < transactionsToShow.size - 1) {
                        Divider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onToggleShow,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    if (showTransactionHistory) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (showTransactionHistory) "Mostrar menos" else "Mostrar más"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (showTransactionHistory) "Mostrar menos" else "Mostrar más")
            }
        }
    }
}

@Composable
fun LoanSection(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF991B1B)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Solicitar Préstamo",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Obtén financiamiento para tus proyectos",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Préstamo pre-aprobado",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Hasta $100,000.00",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    Icons.Default.CurrencyExchange,
                    contentDescription = "Préstamo",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tasa de interés desde 12.5% anual. Plazos de 12 a 60 meses.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("loan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEF4444),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Solicitar", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    var showFrequentUsers by remember { mutableStateOf(false) }
    var showTransactionHistory by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF991B1B)
    val primaryContainer = Color(0xFF7F1D1D)

    val nombreUsuario = UserManager.getNombreUsuario() ?: "Usuario"
    val userEmail = UserManager.getUserEmail() ?: "usuario@email.com"

    val cryptoPortfolio = remember {
        listOf(
            CryptoAsset(
                symbol = "BTC",
                name = "Bitcoin",
                amount = 0.5,
                currentPrice = 65432.21,
                performance = +12.5,
                color = Color(0xFFF7931A),
                priceHistory = generatePriceHistory(65432.21, 0.08)
            ),
            CryptoAsset(
                symbol = "ETH",
                name = "Ethereum",
                amount = 3.2,
                currentPrice = 3456.78,
                performance = +8.2,
                color = Color(0xFF627EEA),
                priceHistory = generatePriceHistory(3456.78, 0.12)
            ),
            CryptoAsset(
                symbol = "SOL",
                name = "Solana",
                amount = 15.5,
                currentPrice = 143.21,
                performance = +25.8,
                color = Color(0xFF00FFA3),
                priceHistory = generatePriceHistory(143.21, 0.18)
            )
        )
    }

    val totalInvestment = cryptoPortfolio.sumOf { it.amount * it.currentPrice }
    val totalPerformance = cryptoPortfolio.map { it.performance }.average()

    val frequentUsers = listOf(
        "Carlos Méndez" to "CM",
        "Ana García" to "AG",
        "Roberto Sánchez" to "RS",
        "María López" to "ML"
    )

    val transactionHistory = listOf(
        TransactionItem("Carlos Méndez", "$1,500.00", "15/03/2024"),
        TransactionItem("Ana García", "$750.00", "10/03/2024"),
        TransactionItem("Roberto Sánchez", "$2,300.00", "05/03/2024"),
        TransactionItem("María López", "$500.00", "01/03/2024"),
        TransactionItem("Juan Pérez", "$1,200.00", "25/02/2024")
    )

    Scaffold(
        topBar = { DashboardTopBar(navController, nombreUsuario) },
        bottomBar = { DashboardBottomBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryContainer, primaryColor, Color(0xFFDC2626))
                    )
                )
                .padding(16.dp)
        ) {
            item { WelcomeSection(nombreUsuario, userEmail) }
            item { CreditCardSection() }
            item { TransferSection(navController) }
            item {
                InvestmentsSection(
                    navController = navController,
                    cryptoPortfolio = cryptoPortfolio,
                    totalInvestment = totalInvestment,
                    totalPerformance = totalPerformance
                )
            }
            item {
                FrequentUsersSection(
                    frequentUsers = frequentUsers,
                    showFrequentUsers = showFrequentUsers,
                    onToggleShow = { showFrequentUsers = !showFrequentUsers },
                    navController = navController
                )
            }
            item {
                TransactionHistorySection(
                    transactionHistory = transactionHistory,
                    showTransactionHistory = showTransactionHistory,
                    onToggleShow = { showTransactionHistory = !showTransactionHistory }
                )
            }
            item { LoanSection(navController) }
        }
    }
}