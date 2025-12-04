package com.mazebank

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mazebank.screens.auth.LoginScreen
import com.mazebank.screens.auth.RegisterScreen
import com.mazebank.screens.dashboard.DashboardScreen
import com.mazebank.screens.investments.InvestmentsScreen
import com.mazebank.screens.investments.TradingScreen
import com.mazebank.screens.loan.LoanScreen
import com.mazebank.screens.loan.LoanSuccessScreen
import com.mazebank.screens.recovery.PasswordRecoveryScreen
import com.mazebank.screens.transfer.AddAccountScreen
import com.mazebank.screens.transfer.AmountScreen
import com.mazebank.screens.transfer.ConfirmationScreen
import com.mazebank.screens.transfer.OtherPersonScreen
import com.mazebank.screens.transfer.SuccessScreen
import com.mazebank.screens.transfer.TransferMainScreen
import com.mazebank.viewmodels.AuthViewModel
import com.mazebank.viewmodels.TransferViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object PasswordRecovery : Screen("password_recovery")
    object Dashboard : Screen("dashboard")
    object Transfer : Screen("transfer")
    object AddAccount : Screen("add_account")
    object Amount : Screen("amount")
    object Confirmation : Screen("confirmation")
    object Success : Screen("success")
    object OtherPerson : Screen("other_person")
    object Investments : Screen("investments")
    object Trading : Screen("trading")
    object Loan : Screen("loan")
    object LoanSuccess : Screen("loan/success")
}

@Composable
fun MazeBankApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Auth Screens
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel()
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = viewModel()  // ✅ CREAR VIEWMODEL
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel  // ✅ PASAR VIEWMODEL
            )
        }
        composable(Screen.PasswordRecovery.route) {
            PasswordRecoveryScreen(navController)
        }

        // Main Screens
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        // Transfer Screens
        composable(Screen.Transfer.route) {
            val transferViewModel: TransferViewModel = viewModel()
            TransferMainScreen(navController, transferViewModel)
        }
        composable(Screen.AddAccount.route) {
            val transferViewModel: TransferViewModel = viewModel()
            AddAccountScreen(navController, transferViewModel)
        }
        composable(Screen.Amount.route) {
            val transferViewModel: TransferViewModel = viewModel()
            AmountScreen(navController, transferViewModel)
        }
        composable(
            "${Screen.Confirmation.route}?monto={monto}",
            arguments = listOf(
                navArgument("monto") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val transferViewModel: TransferViewModel = viewModel()
            val monto = backStackEntry.arguments?.getString("monto") ?: ""
            ConfirmationScreen(navController, monto, transferViewModel)
        }
        composable(
            "${Screen.Success.route}?monto={monto}&referencia={referencia}",
            arguments = listOf(
                navArgument("monto") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("referencia") {
                    type = NavType.StringType
                    defaultValue = "ABC12345"
                }
            )
        ) { backStackEntry ->
            val monto = backStackEntry.arguments?.getString("monto") ?: ""
            val referencia = backStackEntry.arguments?.getString("referencia")
            SuccessScreen(navController, monto, referencia)
        }
        composable(Screen.OtherPerson.route) {
            val transferViewModel: TransferViewModel = viewModel()
            OtherPersonScreen(navController, transferViewModel)
        }

        // Investment Screens
        composable(Screen.Investments.route) {
            InvestmentsScreen(navController)
        }
        composable(
            Screen.Trading.route,
            arguments = listOf(navArgument("operacion") { type = NavType.StringType })
        ) { backStackEntry ->
            val operacion = backStackEntry.arguments?.getString("operacion") ?: "comprar"
            TradingScreen(navController, operacion)
        }

        // Loan Screens
        composable(Screen.Loan.route) {
            LoanScreen(navController)
        }
        composable(
            "${Screen.LoanSuccess.route}/{monto}/{plazo}",
            arguments = listOf(
                navArgument("monto") { type = NavType.StringType },
                navArgument("plazo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val monto = backStackEntry.arguments?.getString("monto") ?: ""
            val plazo = backStackEntry.arguments?.getString("plazo") ?: ""
            LoanSuccessScreen(navController, monto, plazo)
        }
    }
}