package com.example.familytodos.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.familytodos.AuthViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.data.Resource
import com.example.familytodos.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(viewModel: AuthViewModel = hiltViewModel(), navController: NavController) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(value = false) }
    val registerFlow = viewModel?.registerFlow?.collectAsState()
    val maxChar = 35

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )

        Text(
            text = stringResource(id = R.string.sign_in),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )

        TextField(
            value = username,
            onValueChange = { if (it.length <= maxChar) username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface)
        )
        TextField(
            value = password,
            onValueChange = { if (it.length <= maxChar) password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (showPassword) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )

        TextField(
            value = email,
            onValueChange = { if (it.length <= maxChar) email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            colors = TextFieldDefaults.textFieldColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        Button(
            modifier = Modifier.padding(MaterialTheme.spacing.large),
            onClick = {
                viewModel?.register(username, email, password)
            }) {
            Text(text = stringResource(R.string.create_account), fontSize = 18.sp)
        }

        registerFlow?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    val context = LocalContext.current
                    Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG)
                }

                Resource.Loading -> {
                    CircularProgressIndicator()
                }

                is Resource.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screens.MainScreen.route) {
                            //Remove all screens up to MainScreen
                            popUpTo(Screens.MainScreen.route)
                        }
                    }
                }
            }
        }
    }
}