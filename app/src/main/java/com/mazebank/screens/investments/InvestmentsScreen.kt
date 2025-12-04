package com.mazebank.screens.investments

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlin.math.max
import kotlin.random.Random

// Data class para representar una criptomoneda en el portafolio
data class CryptoAsset(
    val symbol: String,
    val name: String,
    val amount: Double,
    val currentPrice: Double,
    val performance: Double,
    val color: Color,
    val priceHistory: List<Double> = emptyList()
)

// Función para generar datos de historial de precios realistas
fun generatePriceHistory(currentPrice: Double, volatility: Double = 0.1): List<Double> {
    val random = Random(System.currentTimeMillis())
    val history = mutableListOf<Double>()
    var price = currentPrice * (1 - volatility / 2) // Empezar un poco más bajo

    // Generar 15 puntos de datos
    repeat(15) {
        // Movimiento aleatorio con tendencia basada en el rendimiento
        val change = (random.nextDouble() - 0.45) * volatility
        price *= (1 + change)
        // Asegurar que el precio no sea negativo
        price = max(price, currentPrice * 0.1)
        history.add(price)
    }

    // Asegurar que el último punto sea el precio actual
    history[history.size - 1] = currentPrice

    return history
}

// Gráfica interactiva para el portafolio
@Composable
fun PortfolioChart(
    cryptoAssets: List<CryptoAsset>,
    modifier: Modifier = Modifier
) {
    val primaryColor = Color(0xFF991B1B)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color(0xFF991B1B), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        if (cryptoAssets.isNotEmpty() && cryptoAssets.any { it.priceHistory.isNotEmpty() }) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawPortfolioChart(cryptoAssets, size)
            }

            // Leyenda de colores
            Column(
                modifier = Modifier.align(Alignment.TopEnd),
                horizontalAlignment = Alignment.End
            ) {
                cryptoAssets.take(3).forEach { crypto ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 2.dp)
                    ) {
                        Text(
                            text = crypto.symbol,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(crypto.color, CircleShape)
                        )
                    }
                }
            }

            // Indicador de rendimiento
            val totalPerformance = cryptoAssets.map { it.performance }.average()
            Text(
                text = "${if (totalPerformance >= 0) "+" else ""}${"%.1f".format(totalPerformance)}%",
                color = if (totalPerformance >= 0) Color(0xFF4ADE80) else Color(0xFFEF4444),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 4.dp, bottom = 4.dp)
            )
        } else {
            Text(
                text = "Cargando datos del mercado...",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun DrawScope.drawPortfolioChart(
    cryptoAssets: List<CryptoAsset>,
    size: androidx.compose.ui.geometry.Size
) {
    // Filtrar activos que tienen historial de precios
    val assetsWithHistory = cryptoAssets.filter { it.priceHistory.isNotEmpty() }
    if (assetsWithHistory.isEmpty()) return

    // Encontrar los valores máximos y mínimos entre todos los activos
    val allPrices = assetsWithHistory.flatMap { it.priceHistory }
    val maxPrice = allPrices.maxOrNull() ?: 1.0
    val minPrice = allPrices.minOrNull() ?: 0.0
    val priceRange = maxPrice - minPrice

    val chartWidth = size.width
    val chartHeight = size.height * 0.8f
    val verticalPadding = size.height * 0.1f

    // Dibujar línea de referencia (precio promedio)
    val avgPrice = allPrices.average()
    val avgY = chartHeight - (((avgPrice - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding

    drawLine(
        color = Color.White.copy(alpha = 0.2f),
        start = Offset(0f, avgY),
        end = Offset(chartWidth, avgY),
        strokeWidth = 1f
    )

    // Dibujar cada criptomoneda
    assetsWithHistory.forEach { crypto ->
        val path = Path()
        val points = crypto.priceHistory

        if (points.size >= 2) {
            // Encontrar el primer punto
            val firstX = 0f
            val firstY = chartHeight - (((points.first() - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding
            path.moveTo(firstX, firstY)

            // Dibujar la línea suavizada
            points.forEachIndexed { index, price ->
                if (index > 0) {
                    val x = (index.toFloat() / (points.size - 1)) * chartWidth
                    val y = chartHeight - (((price - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding

                    // Crear una línea suavizada
                    val prevX = ((index - 1).toFloat() / (points.size - 1)) * chartWidth
                    val prevY = chartHeight - (((points[index - 1] - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding

                    // Usar curvas para suavizar la línea
                    val controlX1 = prevX + (x - prevX) * 0.3f
                    val controlY1 = prevY
                    val controlX2 = prevX + (x - prevX) * 0.7f
                    val controlY2 = y

                    path.cubicTo(controlX1, controlY1, controlX2, controlY2, x, y)
                }
            }

            // Dibujar la línea principal
            drawPath(
                path = path,
                color = crypto.color,
                style = Stroke(width = 2.5f)
            )

            // Dibujar el último punto
            val lastPoint = points.last()
            val lastX = chartWidth
            val lastY = chartHeight - (((lastPoint - minPrice) / priceRange) * chartHeight).toFloat() + verticalPadding

            drawCircle(
                color = crypto.color,
                radius = 4f,
                center = Offset(lastX, lastY)
            )

            // Efecto de sombra/brillo debajo de la línea
            val shadowPath = Path()
            shadowPath.moveTo(0f, chartHeight + verticalPadding)
            shadowPath.lineTo(0f, firstY)
            shadowPath.addPath(path)
            shadowPath.lineTo(chartWidth, chartHeight + verticalPadding)
            shadowPath.close()

            drawPath(
                path = shadowPath,
                color = crypto.color.copy(alpha = 0.1f)
            )
        }
    }

    // Dibujar ejes
    drawLine(
        color = Color.White.copy(alpha = 0.3f),
        start = Offset(0f, verticalPadding),
        end = Offset(0f, chartHeight + verticalPadding),
        strokeWidth = 1f
    )

    drawLine(
        color = Color.White.copy(alpha = 0.3f),
        start = Offset(0f, chartHeight + verticalPadding),
        end = Offset(chartWidth, chartHeight + verticalPadding),
        strokeWidth = 1f
    )
}

// Gráfica individual para cada criptomoneda
@Composable
fun MiniCryptoChart(
    priceHistory: List<Double>,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(40.dp, 20.dp)) {
        if (priceHistory.size >= 2) {
            val maxPrice = priceHistory.maxOrNull() ?: 1.0
            val minPrice = priceHistory.minOrNull() ?: 0.0
            val priceRange = maxPrice - minPrice

            val path = Path()
            val width = size.width
            val height = size.height

            priceHistory.forEachIndexed { index, price ->
                val x = (index.toFloat() / (priceHistory.size - 1)) * width
                val y = height - (((price - minPrice) / priceRange) * height).toFloat()

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 1.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentsScreen(navController: NavController) {
    // Colores del tema principal (igual que el dashboard)
    val primaryColor = Color(0xFF991B1B) // Rojo oscuro
    val primaryContainer = Color(0xFF7F1D1D) // Rojo más oscuro
    val onPrimary = Color.White

    // Datos de ejemplo de criptomonedas en el portafolio
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
                symbol = "ADA",
                name = "Cardano",
                amount = 1000.0,
                currentPrice = 0.45,
                performance = -2.3,
                color = Color(0xFF0033AD),
                priceHistory = generatePriceHistory(0.45, 0.15)
            ),
            CryptoAsset(
                symbol = "SOL",
                name = "Solana",
                amount = 15.5,
                currentPrice = 143.21,
                performance = +25.8,
                color = Color(0xFF00FFA3),
                priceHistory = generatePriceHistory(143.21, 0.18)
            ),
            CryptoAsset(
                symbol = "DOT",
                name = "Polkadot",
                amount = 85.0,
                currentPrice = 6.78,
                performance = +5.7,
                color = Color(0xFFE6007A),
                priceHistory = generatePriceHistory(6.78, 0.13)
            )
        )
    }

    val totalValue = cryptoPortfolio.sumOf { it.amount * it.currentPrice }
    val totalPerformance = cryptoPortfolio.map { it.performance }.average()

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
                        Text("Inversiones", color = onPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryContainer,
                    titleContentColor = onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = onPrimary
                        )
                    }
                }
            )
        }
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
            // RESUMEN DE INVERSIONES
            item {
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Portafolio Cripto",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Valor Total",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "$${"%.2f".format(totalValue)}",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Rendimiento",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${if (totalPerformance >= 0) "+" else ""}${"%.1f".format(totalPerformance)}%",
                                    color = if (totalPerformance >= 0) Color(0xFF4ADE80) else Color(0xFFEF4444),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${cryptoPortfolio.size} activos en cartera",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // OPCIONES DE TRADING
            item {
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Operar Criptomonedas",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Compra y vende activos digitales",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Comprar
                        Button(
                            onClick = {
                                navController.navigate("trading/comprar")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4ADE80), // Verde
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.TrendingUp,
                                contentDescription = "Comprar",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Comprar Cripto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Botón Vender
                        Button(
                            onClick = {
                                navController.navigate("trading/vender")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFEF4444), // Rojo
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.TrendingDown,
                                contentDescription = "Vender",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Vender Cripto", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // PORTAFOLIO DE CRIPTOMONEDAS
            item {
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Tu Portafolio Cripto",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Activos digitales en tu cartera",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Lista de criptomonedas
                        cryptoPortfolio.forEach { crypto ->
                            CryptoAssetItem(
                                crypto = crypto,
                                onItemClick = {
                                    // Aquí puedes navegar a un detalle de la cripto si quieres
                                }
                            )
                            if (crypto != cryptoPortfolio.last()) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

            // GRÁFICA DEL PORTAFOLIO
            item {
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Rendimiento del Portafolio",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Evolución de tus inversiones en tiempo real",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Gráfica interactiva REAL
                        PortfolioChart(
                            cryptoAssets = cryptoPortfolio,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Métricas rápidas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Mejor rendimiento",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "SOL +25.8%",
                                    color = Color(0xFF4ADE80),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Volatilidad",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Media",
                                    color = Color(0xFFFFA500),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Diversificación",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${cryptoPortfolio.size} assets",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // MERCADO CRIPTO
            item {
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
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Mercado Cripto",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Tendencias del mercado digital",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Índices del mercado cripto
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            MarketIndex("Bitcoin Dominance", "54.2%", "+1.2%", true)
                            MarketIndex("Fear & Greed Index", "72", "+8", true)
                            MarketIndex("Total Market Cap", "$1.68T", "+3.5%", true)
                            MarketIndex("BTC Dominance", "54.2%", "+1.2%", true)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CryptoAssetItem(
    crypto: CryptoAsset,
    onItemClick: () -> Unit
) {
    val assetValue = crypto.amount * crypto.currentPrice

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar de la criptomoneda con color único
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(crypto.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = crypto.symbol.take(3),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = crypto.symbol,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = crypto.name,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = "${"%.4f".format(crypto.amount)} ${crypto.symbol}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$${"%.2f".format(assetValue)}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "${if (crypto.performance >= 0) "+" else ""}${"%.1f".format(crypto.performance)}%",
                color = if (crypto.performance >= 0) Color(0xFF4ADE80) else Color(0xFFEF4444),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            // Mini gráfica para cada cripto
            MiniCryptoChart(
                priceHistory = crypto.priceHistory,
                color = crypto.color,
                modifier = Modifier
                    .size(40.dp, 20.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun MarketIndex(
    name: String,
    value: String,
    change: String,
    isPositive: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = change,
                color = if (isPositive) Color(0xFF4ADE80) else Color(0xFFEF4444),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}