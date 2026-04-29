package com.example.smarttodolistapp.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(factory: TodoViewModelFactory) {
    val viewModel: TodoViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Smart To-Do List")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            AddTaskCard(
                text = uiState.currentTaskText,
                isLoading = uiState.isLoading,
                onTextChanged = viewModel::onTaskTextChanged,
                onAddClick = viewModel::addTask
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = uiState.isLoading) {
                Column {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            uiState.errorMessage?.let { message ->
                ErrorCard(message)
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.tasks.isEmpty()) {
                EmptyTasksContent()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.tasks,
                        key = { it.id }
                    ) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { checked ->
                                viewModel.toggleTask(
                                    taskId = task.id,
                                    isCompleted = checked
                                )
                            },
                            onDeleteClick = {
                                viewModel.deleteTask(task.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

