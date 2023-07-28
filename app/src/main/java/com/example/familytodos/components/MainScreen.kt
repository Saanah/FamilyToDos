package com.example.familytodos.components

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.familytodos.AuthViewModel
import com.example.familytodos.GroupViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, viewModel: AuthViewModel, groupViewModel: GroupViewModel) {


        Scaffold(
            topBar = {
                TopBar(navController, viewModel)
            },
            floatingActionButton = { addGroupButton(navController) }
        ) { contentPadding ->
            // Screen content
            contentPadding.calculateTopPadding()
            mainContent(navController, groupViewModel)
        }

}

@Composable
fun mainContent(navController: NavController, groupViewModel: GroupViewModel) {

    groupViewModel.getUserGroupData() //Get user's groups
    val getGroupData = groupViewModel?.group?.collectAsState()  //Save data

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //THINK
        if (getGroupData != null) {

            LazyColumn(modifier = Modifier.padding(top = 64.dp)){
               itemsIndexed(getGroupData.value){
                   index, group -> GroupCard(group = group, onClick = {navController.navigate(
                   Screens.GroupDetailScreen.route+"/${group.id}")})
               }
            }

        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(id = R.string.no_groups),
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    modifier = Modifier.padding(20.dp)
                )
                Text(
                    text = stringResource(id = R.string.join_or_create_group),
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                )
            }
        }
    }
}

@Composable
fun addGroupButton(navController: NavController) {
    FloatingActionButton(
        onClick = {navController.navigate(Screens.CreateGroupScreen.route)},
        modifier = Modifier.size(60.dp),
        shape = CircleShape

    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = "Add group button"
        )
    }
}


