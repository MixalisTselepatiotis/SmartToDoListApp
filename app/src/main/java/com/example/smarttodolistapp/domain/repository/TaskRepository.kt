package com.example.smarttodolistapp.domain.repository

import com.example.smarttodolistapp.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun observeTasks(): Flow<List<TaskItem>>

    suspend fun addTask(text: String)

    suspend fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean)

    suspend fun deleteTask(taskId: Long)
}