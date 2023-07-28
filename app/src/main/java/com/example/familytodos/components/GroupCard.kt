package com.example.familytodos.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.data.model.Group
import com.example.familytodos.ui.theme.spacing

@Composable
fun GroupCard(group: Group, onClick: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium)
            .clickable (onClick = onClick),
        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.small),

        content = {

            Row(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(

                    modifier = Modifier.fillMaxWidth( 0.8f)

                )

                {
                    Text(
                        group.name,
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )

                    Text(
                        group.description,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .padding(MaterialTheme.spacing.medium),
                        style = MaterialTheme.typography.labelLarge,
                        overflow = TextOverflow.Clip
                    )

                }

                group.img?.let { url ->
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(MaterialTheme.spacing.medium))
                            .size(size = 66.dp),
                        painter = rememberAsyncImagePainter(url),
                        contentDescription = group.img_description,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}