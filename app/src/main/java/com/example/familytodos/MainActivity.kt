package com.example.familytodos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.familytodos.ui.theme.FamilyToDosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel by viewModels<AuthViewModel>()
    private val groupViewModel by viewModels<GroupViewModel>()
    private val groupDetailViewModel by viewModels<GroupDetailViewModel>()
    private val createGroupViewModel by viewModels<CreateGroupViewModel>()
    private val searchBarViewModel by viewModels<SearchBarViewModel>()
    private val addGroupMembersViewModel by viewModels<AddGroupMembersViewModel>()
    private val taskViewModel by viewModels<TaskViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            FamilyToDosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Navigaatio komponentti
                    Navigation(
                        authViewModel, groupViewModel, groupDetailViewModel, createGroupViewModel,
                        searchBarViewModel, addGroupMembersViewModel, taskViewModel
                    )
                }
            }
        }
    }
}

