package com.example.familytodos
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.AuthRepository
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository : AuthRepository, //Get auth repository
) : ViewModel (){

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _registerFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val registerFlow: StateFlow<Resource<FirebaseUser>?> = _registerFlow

    private val _deleteFlow = MutableStateFlow<Resource<Any>?>(null)
    val deleteFlow: StateFlow<Resource<Any>?> = _deleteFlow

    val currentUser: FirebaseUser? get() = repository.currentUser

    //Check if user is already logged in
    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.value = Resource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun register(username: String, email: String, password: String) = viewModelScope.launch {
        _registerFlow.value = Resource.Loading
        val result = repository.register(username, email, password)
        _registerFlow.value = result
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _registerFlow.value = null
    }

    fun deleteUser(){
        viewModelScope.launch {
            val result = repository.deleteUser()
            if (result is Resource.Success) {
                _loginFlow.value = null
                _registerFlow.value = null
                _deleteFlow.value = result
            } else if (result is Resource.Failure) {
                _deleteFlow.value = result
            }
        }
    }
}