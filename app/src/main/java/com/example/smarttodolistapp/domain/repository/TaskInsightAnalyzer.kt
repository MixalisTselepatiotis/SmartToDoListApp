package com.example.smarttodolistapp.domain.repository

import com.example.smarttodolistapp.domain.model.TaskAction

interface TaskInsightAnalyzer {

    suspend fun analyzeTask(text: String): List<TaskAction>

    fun close()
}