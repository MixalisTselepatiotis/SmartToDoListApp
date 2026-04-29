package com.example.smarttodolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.smarttodolistapp.data.local.SmartTodoDatabase
import com.example.smarttodolistapp.data.ml.MlKitTaskInsightAnalyzer
import com.example.smarttodolistapp.data.repository.TaskRepositoryImpl
import com.example.smarttodolistapp.domain.repository.TaskInsightAnalyzer
import com.example.smarttodolistapp.presentation.TodoScreen
import com.example.smarttodolistapp.presentation.TodoViewModelFactory
import kotlin.jvm.java


class MainActivity : ComponentActivity() {

    private var insightAnalyzer: TaskInsightAnalyzer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            SmartTodoDatabase::class.java,
            "smart_todo_database"
        ).build()

        insightAnalyzer = MlKitTaskInsightAnalyzer()

        val repository = TaskRepositoryImpl(
            taskDao = database.taskDao(),
            insightAnalyzer = insightAnalyzer!!
        )

        val factory = TodoViewModelFactory(
            repository = repository
        )

        setContent {
            TodoScreen(factory = factory)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        insightAnalyzer?.close()
    }
}
