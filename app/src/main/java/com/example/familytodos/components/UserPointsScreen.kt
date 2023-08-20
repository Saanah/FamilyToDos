package com.example.familytodos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.UserDataViewModel
import com.example.familytodos.ui.theme.spacing

@Composable
fun UserPointsScreen(
    navController: NavController,
    userDataViewModel: UserDataViewModel = hiltViewModel()
) {

    userDataViewModel.getUserInformation()
    userDataViewModel.getUserTaskPoints()
    val userInfo = userDataViewModel.user.collectAsState().value
    val userPoints = userDataViewModel.userPoints.collectAsState().value.totalPoints

    val familyToDosTitle = when {
        userPoints <= 5 -> "Beginner"
        userPoints <= 15 -> "Helpful"
        userPoints <= 30 -> "Hard worker"
        userPoints <= 50 -> "Chore Hustler"
        userPoints <= 99 -> "Medal worthy"
        userPoints >= 100 -> "Master of Chores"
        else -> "Lazy"
    }

    //Check if user has photo
    val userImage = if (userInfo.profile_img.isNullOrEmpty()) {

        painterResource(id = R.drawable.user_img)

    } else {
        rememberAsyncImagePainter(userInfo.profile_img)
    }

    val pointsText = if (userPoints == 1) {
        "You have finished $userPoints task in total! \n Keep it up!"
    } else if (userPoints == 0) {
        "You have currently $userPoints points! \n You'd better go finish some tasks!"
    } else {
        "You have finished $userPoints tasks in total! \n Keep it up!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color( 	255, 251, 120),
                        Color(255, 119, 0)
                    ),
                    startY = 0.2f
                )
            )
            .padding(top = MaterialTheme.spacing.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                .padding(2.dp)
        ) {
            Image(
                modifier = Modifier
                    .size(96.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                painter = userImage,
                contentDescription = "User profile picture",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            "${userInfo.username}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
            color = MaterialTheme.colorScheme.onPrimary
        )

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.spacing.small), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(  	255, 251, 120),
                modifier = Modifier.size(16.dp)
            )
                Text(
                    "${familyToDosTitle}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(MaterialTheme.spacing.small)
                )
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = Color(  	255, 251, 120),
                modifier = Modifier.size(16.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Total points",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            )
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color( 	255, 204, 0))
            ) {
                Text(
                    "$userPoints",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Text(
                "$pointsText",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(MaterialTheme.spacing.large)
            )
        }
    }
}