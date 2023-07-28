package com.example.familytodos.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familytodos.GroupDetailViewModel
import com.example.familytodos.data.model.Group

@Composable
fun GroupDetailScreen(groupId : String, groupDetailViewModel: GroupDetailViewModel){

   groupDetailViewModel.getGroupById(groupId)
    val selectedGroup : Group? = groupDetailViewModel.group.value

    selectedGroup?.let {

        Column() {

            Text("hello ${selectedGroup.name}")
            Text("hello ${selectedGroup.description}")


        }
    }
}