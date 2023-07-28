package com.example.familytodos

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(

    private val firestoreRepository: FirestoreRepository,
    private val groupViewModel: GroupViewModel

) : ViewModel() {

    //THINK
    private val  _message = MutableStateFlow<String>("")
    val message : StateFlow<String> = _message

    private val  _groupId = MutableStateFlow<String>("")
    val groupId : StateFlow<String> = _groupId


    //Function that is called through viewmodel
    fun createGroup(name: String, description: String){

        createGroupFirestore(name, description)
    }


    //Firestore call to create a new group
    private fun createGroupFirestore(name: String, description: String) {

        //Launch coroutine
        viewModelScope.launch {
            _groupId.value = firestoreRepository.createGroup(name, description)
            //THINK
            if (_message.value == "Group successfully created!") {
                groupViewModel.getUserGroupData()
            }
        }
    }
}