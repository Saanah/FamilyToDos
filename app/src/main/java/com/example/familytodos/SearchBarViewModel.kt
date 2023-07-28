package com.example.familytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Viewmodel for searching users
@HiltViewModel
class SearchBarViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository //Get Firestore repository
) : ViewModel() {

    private val _foundUsers: MutableStateFlow<MutableList<User>> = MutableStateFlow(mutableListOf())
    val foundUsers: StateFlow<MutableList<User>> = _foundUsers

    private val _searchTerm = MutableStateFlow("")
    val searchTerm: StateFlow<String> = _searchTerm


    //Find the user(s) that match the given search term and add them to the list
    init {
        viewModelScope.launch {
            _searchTerm.collect { searchTerm ->
                val users = firestoreRepository.findUser(searchTerm)
                _foundUsers.value = users
            }
        }
    }

    //Update the search term
    fun onSearchTermChange(newSearchTerm: String) {

        viewModelScope.launch {
            _searchTerm.value = newSearchTerm
        }
    }

    //clear foundUsers
    fun clearFoundUsers() {
        _foundUsers.value.clear()
    }
}