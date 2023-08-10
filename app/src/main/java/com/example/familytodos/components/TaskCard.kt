package com.example.familytodos.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.familytodos.ui.theme.spacing

@Composable
fun TaskCard(task : String, isCompleted : Boolean, onChecked: (Boolean) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
            .clickable(onClick = {}),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.small),
        shape = RoundedCornerShape(MaterialTheme.spacing.small),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){

            Text(text = task)

            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = if (isCompleted) "Done" else "Not done",
                tint = if (isCompleted) Color.Green else Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onChecked(!isCompleted)
                    }
            )
        }

    }

}