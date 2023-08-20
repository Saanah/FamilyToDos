package com.example.familytodos.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.EditableText
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.familytodos.AuthViewModel
import com.example.familytodos.R
import com.example.familytodos.Screens
import com.example.familytodos.UserDataViewModel
import com.example.familytodos.data.Resource
import com.example.familytodos.ui.theme.spacing
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    userDataViewModel: UserDataViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    userDataViewModel.getUserInformation()
    val userInfo = userDataViewModel.user.collectAsState().value

    val deleteResult = authViewModel.deleteFlow.collectAsState().value

    var isEditingDescription by remember { mutableStateOf(false) }
    // State to hold the edited description
    var editedDescription by remember { mutableStateOf(userInfo.profile_desc) }
    // Failure message for user deletion
    var deletionMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    if (deletionMessage.isNotBlank()) {
        Toast.makeText(context, deletionMessage, Toast.LENGTH_SHORT).show()
    }

    // Check if deleteResult changes and call deleteUserNavigateOut to navigate user back to login screen
    LaunchedEffect(deleteResult) {
        if (deleteResult != null) {
            deleteUserNavigateOut(deleteResult, navController) { updatedMessage ->
                deletionMessage = updatedMessage
            }
        }
    }

    //Check if user has photo
    val userImage = if (userInfo.profile_img.isNullOrEmpty()) {
        painterResource(id = R.drawable.user_img)
    } else {
        rememberAsyncImagePainter(userInfo.profile_img)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
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

        Text(
            text = userInfo.profile_desc,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Set the spacer's width to 50% of the available width
                    .height(16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium)
            ) {

                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Email Icon",
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)

                    )

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit description",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = MaterialTheme.spacing.small)
                            .clickable {
                                isEditingDescription = !isEditingDescription
                            }
                    )
                }

                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "${userInfo.email}",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
                    )
                    Text(
                        text = "Edit profile",
                        style = MaterialTheme.typography.bodyLarge,
                    )

                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isEditingDescription) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = editedDescription,
                            onValueChange = {
                                editedDescription = it
                            },
                            label = { Text("Edit profile") },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = androidx.compose.ui.text.input.ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    // Save the edited description and exit edit mode
                                    isEditingDescription = false
                                }
                            )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(top = MaterialTheme.spacing.medium),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            OutlinedButton(
                                onClick = { isEditingDescription = false },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = MaterialTheme.spacing.small),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.error,
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    userDataViewModel.updateUserProfileDescription(
                                        editedDescription
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = MaterialTheme.spacing.small),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "Save",
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = { authViewModel.deleteUser() },
                modifier = Modifier.fillMaxWidth(0.7f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.error,
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = stringResource(id = R.string.delete_account),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

//Check if user deletion was succesfully and go to login screen. If not update message.
fun deleteUserNavigateOut(
    deleteResult: Resource<Any>,
    navController: NavController,
    updateMessage: (String) -> Unit
) {

    when (deleteResult) {
        is Resource.Success -> {
            updateMessage("Account deleted succesfully")
            navController.navigate(Screens.LoginScreen.route){
                popUpTo(Screens.AccountScreen.route) { //Prevent user from going back to this screen when pressing back
                    inclusive = true
                    saveState = true
                }
            }
        }

        is Resource.Failure -> updateMessage("Failed to delete user: ${deleteResult.exception.message}")
        else -> updateMessage("Unknown error")
    }
}



