package com.example.familytodos.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.GroupViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.ui.theme.spacing

@Composable
fun GroupInfoScreen(groupViewModel: GroupViewModel, groupId: String, navController: NavController) {

    groupViewModel.getGroupInfoById(groupId)

    val groupInfo = groupViewModel.groupInfo.collectAsState().value
    val groupMembers = groupInfo.members
    val memberAmount = groupInfo.members.size
    val title = "Group info"

    AppScaffold(
        topBar = { MenuItemTopBar(title, navController) },
        floatingActionButton = {
            AddButton(navController = navController) {
                navController.navigate(route = Screens.AddGroupMembersScreen.route + "/${groupId}")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.spacing.medium
                )
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${groupInfo.name}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            )
            Text(
                text = "${groupInfo.description}",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.large)
            )
            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Members",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
                Text(
                    text = "$memberAmount members",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
            }
            LazyColumn(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)) {

                items(groupMembers) { member ->
                    memberCard(
                        username = member.username,
                        profile_desc = member.profile_desc,
                        profile_img = member.profile_img
                    ) {
                        groupViewModel.deleteMemberFromGroup(member.userId, groupId) //Delete member from group
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun memberCard(username: String, profile_desc: String, profile_img: String, onDelete: () -> Unit) {

    val alertVisible = remember { mutableStateOf(false) }

    val onLongPress: () -> Unit = {
        // Show delete confirmation dialog
        alertVisible.value = true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
            .combinedClickable(onClick = {}, onLongClick = onLongPress),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.extraSmall),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val userImage = if (profile_img.isNullOrEmpty()) {

                painterResource(id = R.drawable.user_img)

            } else {
                rememberAsyncImagePainter(profile_img)
            }
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                    .padding(2.dp)
            ){
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter = userImage,
                contentDescription = "User profile picture"
            )
            }
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth()
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
                )
                Text(text = profile_desc, style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    //If card is pressed, show alert for deleting a member
    if (alertVisible.value) {
        AlertDialog(
            onDismissRequest = {
                alertVisible.value = false
            },
            title = { Text(text = "Remove member?", style = MaterialTheme.typography.titleLarge) },
            text = {
                Text(
                    text = "Are you sure you want to remove $username from the group?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },

            confirmButton = {
                Text(
                    text = "Delete",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(
                            start = MaterialTheme.spacing.large,
                            end = MaterialTheme.spacing.small
                        )
                        .clickable {
                            onDelete()
                            alertVisible.value = false
                        }
                )
            },
            dismissButton = {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable {
                        alertVisible.value = false
                    }
                )
            }
        )
    }
}
