package com.example.familytodos.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

//Scaffold for the whole app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    topBar: @Composable () -> Unit,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    if (floatingActionButton != null) {
        Scaffold(
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            content = { paddingValues ->
                content(paddingValues)
            }
        )
    }
}
