package com.example.familytodos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.familytodos.CreateGroupViewModel
import com.example.familytodos.Screens
import com.example.familytodos.data.model.User
import com.example.familytodos.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    createGroupViewModel: CreateGroupViewModel,
    navController: NavController
) {

    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    val groupId by createGroupViewModel.groupId.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Group name") },
            singleLine = true,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
        TextField(
            value = groupDescription,
            onValueChange = { groupDescription = it },
            label = { Text("Description") },
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )

        Button(onClick = {
            createGroupViewModel.createGroup(groupName, groupDescription)
        }) {
            Text("Create")
        }
    }
    // Observe when the groupId is not empty and allow navigation
    LaunchedEffect(groupId) {
        if (groupId.isNotEmpty()) {
            navController.navigate("${Screens.AddGroupMembersScreen.route}/${groupId}")
            createGroupViewModel.clearGroupId()
        }
    }
}
