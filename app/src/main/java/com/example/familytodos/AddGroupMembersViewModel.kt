package com.example.familytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Viewmodel for updating the created group with the selected users
@HiltViewModel
class AddGroupMembersViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository //Get Firestore repository
) : ViewModel() {

    fun addSelectedUsersToGroup(groupId: String, selectedUsers: List<User>) {


        viewModelScope.launch {

            firestoreRepository.addSelectedUsersToGroup(groupId, selectedUsers)

        }
    }
}