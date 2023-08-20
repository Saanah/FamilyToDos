package com.example.familytodos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.familytodos.AddGroupMembersViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.SearchBarViewModel
import com.example.familytodos.data.model.User

@Composable
fun AddGroupMembersScreen(searchBarViewModel: SearchBarViewModel, addGroupMembersViewModel: AddGroupMembersViewModel, navController: NavController, groupId: String) {

    val foundUsers by searchBarViewModel.foundUsers.collectAsState()
    val selectedUsers = remember { mutableStateListOf<User>() } //selected group members' user ids are saved in a list

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add members to your group!")
        SearchBar(searchBarViewModel)
        
        LazyColumn(){
            items(selectedUsers){selectedUser ->
                Text(text = selectedUser.username)
            }
        }

        // Display the foundUsers list in a LazyColumn
        LazyColumn(
        ) {
            items(foundUsers) { user ->

                val isSelected =
                    selectedUsers.contains(user) //If user is in the list and selected return true, otherwise false

                UserCard(user = user, isSelected = isSelected, isImage = true, onClick = {
                    if (isSelected) selectedUsers.remove(user) //if user is already selected, delete the user from list
                    else selectedUsers.add(user)
                })
            }
        }
        
        Button(onClick = {
            addGroupMembersViewModel.addSelectedUsersToGroup(groupId, selectedUsers)
            navController.navigate(route ="${Screens.GroupDetailScreen.route}/${groupId}"){
                popUpTo(Screens.MainScreen.route) {
                    inclusive = true // Include the destination CreateGroupScreen in the removal
                }
            }
            searchBarViewModel.clearFoundUsers()}
          ) {
            Text(text = stringResource(R.string.add_selected_users))
        }
    }
}
