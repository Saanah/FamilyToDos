package com.example.familytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.Points
import com.example.familytodos.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository  //Get Firestore repository
) : ViewModel() {

    private val _user : MutableStateFlow<User> = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _userPoints : MutableStateFlow<Points> = MutableStateFlow(Points())
    val userPoints: StateFlow<Points> = _userPoints

    fun getUserInformation(){

        viewModelScope.launch {

            _user.value = firestoreRepository.getUserInformation()
        }
    }

    fun getUserTaskPoints(){

        viewModelScope.launch {
            _userPoints.value = firestoreRepository.getUserTaskPoints()
        }
    }

    fun updateUserProfileDescription(profile_desc : String){

        viewModelScope.launch {
            firestoreRepository.updateUserProfileDescription(profile_desc)
            getUserInformation()
        }
    }

}