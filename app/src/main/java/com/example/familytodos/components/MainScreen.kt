package com.example.familytodos.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.familytodos.GroupViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.ui.theme.spacing

@Composable
fun MainScreen(navController: NavController, groupViewModel: GroupViewModel) {

    val title = "FamilyToDos"

    AppScaffold(
        topBar = { TopBar(title, navController) },
        floatingActionButton = {
            AddButton(
                navController = navController
            ) {
                navController.navigate(route = Screens.CreateGroupScreen.route)
            }
        }
    ) {
        mainContent(navController, groupViewModel)
    }

}

@Composable
fun mainContent(navController: NavController, groupViewModel: GroupViewModel) {

    groupViewModel.getUserGroupData() //Get user's groups
    val getGroupData = groupViewModel?.group?.collectAsState()  //Save data

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //If user has groups show them, otherwise tell user to create a group
        getGroupData?.value?.let {
            if (getGroupData.value.size != 0) {

                LazyColumn(modifier = Modifier.padding(top = 64.dp)) {
                    items(getGroupData.value) { group ->
                        GroupCard(group = group, onClick = {
                            navController.navigate(
                                Screens.GroupDetailScreen.route + "/${group.id}"
                            )
                        })
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
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
}

