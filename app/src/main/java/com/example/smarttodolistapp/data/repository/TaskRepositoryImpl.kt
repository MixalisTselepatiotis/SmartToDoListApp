package com.example.smarttodolistapp.data.repository

import com.example.smarttodolistapp.data.local.TaskDao
import com.example.smarttodolistapp.data.local.TaskEntity
import com.example.smarttodolistapp.data.mapper.toActionsText
import com.example.smarttodolistapp.data.mapper.toDomain
import com.example.smarttodolistapp.domain.model.TaskItem
import com.example.smarttodolistapp.domain.repository.TaskInsightAnalyzer
import com.example.smarttodolistapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val insightAnalyzer: TaskInsightAnalyzer
) : TaskRepository {

    override fun observeTasks(): Flow<List<TaskItem>> {
        return taskDao.observeTasks()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun addTask(text: String) {
        val actions = insightAnalyzer.analyzeTask(text)

        val entity = TaskEntity(
            text = text,
            isCompleted = false,
            actionsText = actions.toActionsText(),
            createdAt = System.currentTimeMillis()
        )

        taskDao.insertTask(entity)
    }

    override suspend fun toggleTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        taskDao.updateTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted
        )
    }

    override suspend fun deleteTask(taskId: Long) {
        taskDao.deleteTask(taskId)
    }
}