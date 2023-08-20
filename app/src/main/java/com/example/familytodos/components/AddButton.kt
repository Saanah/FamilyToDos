package com.example.familytodos.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.familytodos.Screens

@Composable
fun AddButton(navController: NavController, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add button"
        )
    }
}
