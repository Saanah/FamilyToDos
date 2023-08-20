package com.example.familytodos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.familytodos.data.FirestoreRepository
import com.example.familytodos.data.model.Task
import com.example.familytodos.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository //Get Firestore repository
) : ViewModel() {

    private var _task: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableListOf())
    val task: StateFlow<List<Task>> = _task

    private var _allTasks: MutableStateFlow<MutableList<Task>> = MutableStateFlow(mutableListOf())
    val allTasks: StateFlow<MutableList<Task>> = _allTasks

    fun getUserTasks(groupId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            _task.value = firestoreRepository.getUserTasks(groupId)

        }
    }

    fun getAllTasksFromGroup(groupId: String) {

        viewModelScope.launch(Dispatchers.IO) {

            _allTasks.value = firestoreRepository.getAllTasksFromGroup(groupId)

        }
    }

    fun changeTaskStatus(task: Task, groupId: String, isCompleted: Boolean, points: Int) {

        val currentUser = firestoreRepository.currentUser

        viewModelScope.launch(Dispatchers.IO) {

            //If task belongs to the current user, allow status change
            if (currentUser?.uid == task.userId) {
                firestoreRepository.changeTaskStatus(task.id, isCompleted)
                getUserTasks(groupId)
                getAllTasksFromGroup(groupId)
                addTaskPoints(groupId, task.username, points)
            }
        }
    }

    fun deleteTask(taskId: String, groupId: String){

        viewModelScope.launch {
            firestoreRepository.deleteTask(taskId)
            getUserTasks(groupId)
            getAllTasksFromGroup(groupId)
        }
    }

    private fun addTaskPoints(groupId: String, username : String, points : Int){

        viewModelScope.launch {
            firestoreRepository.addOrDeleteCompletedTaskPoints(groupId, username, points)
        }

    }

}