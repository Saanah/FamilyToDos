package com.example.familytodos.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.familytodos.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: String,
    isCompleted: Boolean,
    isMemberTask: Boolean,
    username: String,
    onChecked: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    val alertVisible = remember { mutableStateOf(false) }

    //Cross out text if task is done
    val taskText = if (isCompleted) {
        buildAnnotatedString {
            append(task)
            addStyle(
                style = SpanStyle(textDecoration = TextDecoration.LineThrough),
                start = 0,
                end = task.length
            )
        }
    } else {
        AnnotatedString(task)
    }

    val onLongPress: () -> Unit = {
        // Show delete confirmation dialog
        alertVisible.value = true
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small)
            .combinedClickable(onClick = {}, onLongClick = onLongPress),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.spacing.small),
        shape = RoundedCornerShape(MaterialTheme.spacing.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //Show members names on a task
            if (isMemberTask) {
                Text(
                    text = username,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(text = taskText, modifier = Modifier.padding(end = MaterialTheme.spacing.small))

            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = if (isCompleted) "Done" else "Not done",
                tint = if (isCompleted) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onChecked(!isCompleted)
                    }
            )
        }
    }

    //If card is pressed, show alert for deleting a task
    if (alertVisible.value) {
        AlertDialog(
            onDismissRequest = {
                alertVisible.value = false
            },
            title = { Text(text = "Delete task?", style = MaterialTheme.typography.titleLarge)},
            text = {
                Text(
                    text = "Are you sure you want to delete this task?",
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
