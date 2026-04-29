package com.example.smarttodolistapp.presentation

import com.example.smarttodolistapp.domain.model.TaskItem

data class TodoUiState(
    val currentTaskText: String = "",
    val tasks: List<TaskItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)