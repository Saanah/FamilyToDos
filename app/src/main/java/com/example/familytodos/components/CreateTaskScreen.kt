package com.example.familytodos.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.familytodos.GroupViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.User
import com.example.familytodos.ui.theme.spacing

@Composable
fun CreateTaskScreen(navController: NavController, groupViewModel: GroupViewModel) {

    val tasks = arrayOf(
        "Vacuum", "Wash dishes", "Do laundry", "Mop the floors",
        "Take the trash out", "Clean the bathroom", "Water plants", "Take the dog out",
        "Cooking", "Feed the pets"
    )

    groupViewModel.getGroupInfoById("Hfa4igeu9tBaLK841goV")
    val groupInfo = groupViewModel.groupInfo.collectAsState()
    var selectedUser by remember { mutableStateOf<User?>(null) }
    var selectedTask by remember { mutableStateOf(tasks[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaskDropdownMenu(tasks, selectedTask) { task ->
            selectedTask = task
        }
        SelectUserForTask(groupInfo.value, selectedUser) { user ->
            selectedUser = user
        }
        Button(onClick = {
            selectedUser?.let { selectedUser ->
                groupViewModel.createTaskForGroup(
                    "Hfa4igeu9tBaLK841goV",
                    selectedTask,
                    selectedUser
                );
                navController.navigate(Screens.MainScreen.route) //Vaihda groupDetailScreen
            }
        }) {

            Text(text = stringResource(id = R.string.assign))

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDropdownMenu(tasks: Array<String>, selectedTask: String, onSelectedTask: (String) -> Unit) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.large)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedTask,
                singleLine = true,
                onValueChange = { onSelectedTask(it) },  //TextField for user's own inputs
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tasks.forEach { item ->     //Show list of default tasks in a dropdown menu
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            onSelectedTask(item)
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectUserForTask(groupInfo: Group, selectedUser: User?, onUserSelected: (User?) -> Unit) {

    val users = groupInfo.members

    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {

        items(users) { user ->

            val isSelected = selectedUser == user

            UserCard(user = user, isSelected, onClick = {
                onUserSelected(if (isSelected) null else user) //lambda for createTaskScreen
            })
        }
    })
}
