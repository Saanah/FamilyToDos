package com.example.familytodos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.R
import com.example.familytodos.data.model.User
import com.example.familytodos.ui.theme.spacing

//Show group members in a card
@Composable
fun UserCard(user: User, isSelected: Boolean, onClick: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium)
            .clickable(onClick = {
                onClick(user.userId)
            }),
        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.Green else MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.small)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val userImage = if (user.profile_img.isNullOrEmpty()) {

                painterResource(id = R.drawable.user_img)

            } else {
                rememberAsyncImagePainter(user.profile_img)
            }
            Text(text = user.username)
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .size(size = 80.dp)
                    .clip(shape = RoundedCornerShape(MaterialTheme.spacing.medium)),

                painter = userImage,
                contentDescription = "User profile picture",
                contentScale = ContentScale.Crop
            )
        }
    }
}