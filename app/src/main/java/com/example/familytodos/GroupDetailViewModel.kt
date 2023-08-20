package com.example.familytodos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.Group
import com.example.familytodos.data.model.Points
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository //Get Firestore repository
) : ViewModel() {

    private val _group : MutableStateFlow<Group> = MutableStateFlow(Group())
    val group: StateFlow<Group> = _group

    private val _points : MutableStateFlow<MutableList<Points>> = MutableStateFlow(mutableListOf())
    val points: StateFlow<MutableList<Points>> = _points

    fun getGroupById(groupId: String) {

        viewModelScope.launch {

            val fetchedGroup : Group = firestoreRepository.getGroupById(groupId)
           _group.value = fetchedGroup
        }
    }
    fun getGroupTaskPointsPerUser(groupId: String){

        viewModelScope.launch {

            _points.value = firestoreRepository.getGroupTaskPointsPerUser(groupId)
        }
    }


}