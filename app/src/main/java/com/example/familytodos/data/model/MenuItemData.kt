package com.example.familytodos.data.model

import androidx.compose.ui.graphics.vector.ImageVector

//Data class for menu items
data class MenuItemData(val text: String, val icon: ImageVector, val route: String, val groupId : String? = null)
