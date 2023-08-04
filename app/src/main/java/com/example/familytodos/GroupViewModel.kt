package com.example.familytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository  //Get Firestore repository
) : ViewModel() {

    //private val _group = MutableLiveData<Group>()

    private val _group : MutableStateFlow<MutableList<Group>> = MutableStateFlow(mutableListOf())
    val group: StateFlow<MutableList<Group>> = _group

    private val _groupInfo : MutableStateFlow<Group> = MutableStateFlow(Group())
    val groupInfo : StateFlow<Group> = _groupInfo

    //Firestore call to get all user's groups
    fun getUserGroupData() {

        //Launch coroutine
        viewModelScope.launch {
            _group.value = firestoreRepository.getUserGroups()
        }
    }

    //Firestore call to get group's information by id
    fun getGroupInfoById(groupId : String) {

        viewModelScope.launch {
            _groupInfo.value = firestoreRepository.getGroupById(groupId)
        }
    }

    //Create task for the group
    fun createTaskForGroup(groupId: String, task : String, user : User){

        val username = user.username
        val userId = user.userId

        viewModelScope.launch {
            firestoreRepository.createTask(groupId, task, username, userId, false)
        }
    }
}