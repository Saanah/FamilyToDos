package com.example.familytodos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.AddGroupMembersViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.SearchBarViewModel
import com.example.familytodos.data.model.User
import com.example.familytodos.ui.theme.spacing

@Composable
fun AddGroupMembersScreen(searchBarViewModel: SearchBarViewModel, addGroupMembersViewModel: AddGroupMembersViewModel, navController: NavController, groupId: String) {

    val foundUsers by searchBarViewModel.foundUsers.collectAsState()
    val selectedUsers = remember { mutableStateListOf<User>() } //selected group members are saved in a list

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

                UserCard(user, isSelected, onClick = {
                    if (isSelected) selectedUsers.remove(user) //if user is already selected, delete the user from list
                    else selectedUsers.add(user)
                })
            }
        }
        
        Button(onClick = {

            addGroupMembersViewModel.addSelectedUsersToGroup(groupId, selectedUsers)
            navController.navigate(Screens.MainScreen.route)
            searchBarViewModel.clearFoundUsers()}
          ) {
            Text(text = stringResource(R.string.add_selected_users))
        }
    }
}

