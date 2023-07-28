package com.example.familytodos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.Group
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository //Get Firestore repository
) : ViewModel() {

    private val _group = MutableLiveData<Group>()
    val group: LiveData<Group> = _group

    fun getGroupById(groupId: String) {

        viewModelScope.launch {

            val fetchedGroup : Group = firestoreRepository.getGroupById(groupId)
           _group.value = fetchedGroup
        }
    }


}