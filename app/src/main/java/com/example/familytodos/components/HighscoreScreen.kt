package com.example.familytodos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.familytodos.GroupDetailViewModel
import com.example.familytodos.ui.theme.spacing

@Composable
fun HighscoreScreen(
    groupDetailViewModel: GroupDetailViewModel,
    groupId: String,
    navController: NavController
) {

    val title = "Highscores"
    AppScaffold(
        topBar = { MenuItemTopBar(title, navController) },
        floatingActionButton = {
        }
    ) { paddingValues ->

        groupDetailViewModel.getGroupTaskPointsPerUser(groupId)
        val memberPoints = groupDetailViewModel.points.collectAsState().value

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = MaterialTheme.spacing.medium)
        ) {

            items(memberPoints.size) { index ->
                val member = memberPoints[index]
                val placement = index + 1 // Calculate placement starting from 1
                HighscoreUserCard(
                    username = member.username,
                    totalPoints = member.totalPoints,
                    placement = placement
                )
            }
        }
    }
}

@Composable
fun HighscoreUserCard(username: String, totalPoints: Int, placement: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.extraSmall),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(MaterialTheme.spacing.small),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$placement",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )

            }
            Text(
                text = if (totalPoints == 1) "$totalPoints point" else "$totalPoints points",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall)
            )
        }
    }
}