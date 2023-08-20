package com.example.familytodos.components

import WeekdayAndDate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.familytodos.GroupDetailViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.TaskViewModel
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.MenuItemData
import com.example.familytodos.ui.theme.spacing

@Composable
fun GroupDetailScreen(groupId : String, groupDetailViewModel: GroupDetailViewModel, taskViewModel: TaskViewModel, navController: NavController) {

    taskViewModel.getUserTasks(groupId)
    taskViewModel.getAllTasksFromGroup(groupId)
    groupDetailViewModel.getGroupById(groupId)

    val selectedGroup = groupDetailViewModel.group.collectAsState().value
    val userTasks = taskViewModel.task.collectAsState().value
    val allTasks = taskViewModel.allTasks.collectAsState().value
    val title = "${selectedGroup?.name}"
    val menuItems = getMenuItemsForGroupDetailScreen(groupId)


    AppScaffold(
        topBar = { TopBar(title, navController, menuItems) },
        floatingActionButton = { AddButton(navController = navController){
            navController.navigate(Screens.CreateTaskScreen.route + "/${groupId}")
        } }
    ) {

        paddingValues ->

        selectedGroup?.let {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues)) {

                WeekdayAndDate()

                Text(
                    text = stringResource(R.string.your_tasks),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                )

                Divider()

                //User tasks
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium)
                ) {
                    items(userTasks) { task ->


                        TaskCard(task = task.task,
                            isMemberTask = false,
                            username = task.username,
                            isCompleted =  task.isCompleted,
                            onChecked = { isCompleted ->
                                var points = if (isCompleted) 1 else -1 //Add or remove points depending on is task marked done or undone
                                taskViewModel.changeTaskStatus(task, groupId, isCompleted, points) //Mark user task as done/undone
                            },
                            onDelete = {
                                taskViewModel.deleteTask(task.id, groupId)                 //Delete user task
                            })
                    }
                }

                Text(
                    text = stringResource(R.string.family_tasks),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                )
                Divider()

                //All tasks
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium)
                ) {
                    items(allTasks) { task ->

                        TaskCard(
                            task = task.task,
                            isMemberTask = true,
                            username = task.username,
                            isCompleted =  task.isCompleted,
                            onChecked = { isCompleted ->
                                var points = if (isCompleted) 1 else -1
                                taskViewModel.changeTaskStatus(task, groupId, isCompleted, points)
                            },
                            onDelete = {
                                taskViewModel.deleteTask(task.id, groupId)
                            })
                    }
                }
            }
        }
    }
}

//Menu items for GroupDetailScreen
fun getMenuItemsForGroupDetailScreen(groupId: String): ArrayList<MenuItemData> {

    val listItems = ArrayList<MenuItemData>()

    listItems.add(
        MenuItemData(
            text = "Account",
            icon = Icons.Outlined.Person,
            route = Screens.AccountScreen.route
        )
    )
    listItems.add(
        MenuItemData(
            text = "Group info",
            icon = Icons.Outlined.Info,
            route = Screens.GroupInfoScreen.route,
            groupId = groupId
        )
    )
    listItems.add(
        MenuItemData(
            text = "Highscores",
            icon = Icons.Outlined.Star,
            route = Screens.HighscoreScreen.route,
            groupId = groupId
        )
    )
    listItems.add(
        MenuItemData(
            text = "Log out",
            icon = Icons.Outlined.Logout,
            route = Screens.LoginScreen.route
        )
    )

    return listItems
}