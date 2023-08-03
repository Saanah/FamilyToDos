package com.example.familytodos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.familytodos.GroupDetailViewModel
import com.example.familytodos.Screens
import com.example.familytodos.data.model.Group

@Composable
fun GroupDetailScreen(groupId : String, groupDetailViewModel: GroupDetailViewModel, navController: NavController){

   groupDetailViewModel.getGroupById(groupId)
    val selectedGroup : Group? = groupDetailViewModel.group.value

    selectedGroup?.let {

        Column() {

            Text("hello ${selectedGroup.name}")
            Text("hello ${selectedGroup.description}")

            Button(onClick = { navController.navigate(Screens.CreateTaskScreen.route)}) {
                
                Text(text = "Add task")

            }


        }
    }
}