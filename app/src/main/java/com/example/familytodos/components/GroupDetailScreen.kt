package com.example.familytodos.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.familytodos.GroupDetailViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.TaskViewModel
import com.example.familytodos.data.model.Group
import com.example.familytodos.ui.theme.spacing

@Composable
fun GroupDetailScreen(groupId : String, groupDetailViewModel: GroupDetailViewModel, taskViewModel: TaskViewModel, navController: NavController){

    taskViewModel.getUserTasks(groupId)
    taskViewModel.getAllTasksFromGroup(groupId)
    groupDetailViewModel.getGroupById(groupId)

    val selectedGroup : Group? = groupDetailViewModel.group.value
    val userTasks = taskViewModel.task.collectAsState().value
    val allTasks = taskViewModel.allTasks.collectAsState().value

    selectedGroup?.let {

        Column() {

            Text("hello ${selectedGroup.name}")
            Text("hello ${selectedGroup.description}")

            Button(onClick = { navController.navigate(Screens.CreateTaskScreen.route+"/${groupId}")}) {

                Text(text = "Add new task")

            }

            Text(
                text = stringResource(R.string.your_tasks),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            )

            Divider()

            LazyColumn(modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth(),
            contentPadding =  PaddingValues(MaterialTheme.spacing.medium)
            ){
                items(userTasks){ task ->


                    TaskCard(task = task.task, task.isCompleted){ isCompleted ->

                        taskViewModel.changeTaskStatus(isCompleted, task.id, groupId)
                    }

                }
            }

            Text(
                text = stringResource(R.string.all_tasks),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            )
            Divider()

            LazyColumn(modifier = Modifier.fillMaxSize(),
                contentPadding =  PaddingValues(MaterialTheme.spacing.medium)
            ){
                items(allTasks){ task ->

                    TaskCard(task = task.task, task.isCompleted){ isCompleted ->

                        taskViewModel.changeTaskStatus(isCompleted, task.id, groupId)
                    }

                }
            }
        }
    }
}