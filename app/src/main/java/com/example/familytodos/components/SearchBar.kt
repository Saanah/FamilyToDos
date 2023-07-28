package com.example.familytodos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.familytodos.SearchBarViewModel
import com.example.familytodos.ui.theme.spacing

//Search bar for searching users in the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchBarViewModel: SearchBarViewModel) {

    var searchTerm by remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

        TextField(
            value = searchTerm,
            onValueChange = { searchTerm = it; searchBarViewModel.onSearchTermChange(searchTerm);}, //Give the search term to the SearchBarViewodel
            placeholder = { Text(text = "Search user") },
            singleLine = true,
            modifier = Modifier.padding(
                MaterialTheme.spacing.medium
            )
        )

    }
}

