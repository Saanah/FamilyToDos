package com.example.familytodos

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(viewModel: AuthViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
        composable(route = Screens.LoginScreen.route){
            LoginScreen(viewModel = viewModel, navController = navController)
        }
        composable(route = Screens.MainScreen.route){
            MainScreen(navController = navController)
        }
        
        composable(route = Screens.RegisterScreen.route)
        {
            RegisterScreen(navController = navController)
        }
    }
}
