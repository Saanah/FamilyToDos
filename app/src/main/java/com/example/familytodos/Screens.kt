package com.example.familytodos

sealed class Screens(val route: String) {

    object LoginScreen : Screens("login_screen")
    object MainScreen : Screens("main_screen")
    object RegisterScreen : Screens("register_screen")
}
