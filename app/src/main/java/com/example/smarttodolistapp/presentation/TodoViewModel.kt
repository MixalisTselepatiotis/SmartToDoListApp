package com.example.smarttodolistapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttodolistapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.observeTasks().collect { tasks ->
                _uiState.update {
                    it.copy(tasks = tasks)
                }
            }
        }
    }

    fun onTaskTextChanged(newText: String) {
        _uiState.update {
            it.copy(currentTaskText = newText)
        }
    }

    fun addTask() {
        val text = _uiState.value.currentTaskText.trim()

        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                repository.addTask(text)

                _uiState.update {
                    it.copy(
                        currentTaskText = "",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Could not add task"
                    )
                }
            }
        }
    }

    fun toggleTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(
                taskId = taskId,
                isCompleted = isCompleted
            )
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}