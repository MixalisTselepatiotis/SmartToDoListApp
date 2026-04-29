package com.example.smarttodolistapp.domain.model

data class TaskItem(
    val id: Long = 0,
    val text: String,
    val isCompleted: Boolean = false,
    val actions: List<TaskAction> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)