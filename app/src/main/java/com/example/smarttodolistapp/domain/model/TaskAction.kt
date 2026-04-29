package com.example.smarttodolistapp.domain.model

data class TaskAction(
    val start: Int,
    val end: Int,
    val value: String,
    val type: TaskActionType
)

enum class TaskActionType {
    PHONE,
    EMAIL,
    URL,
    ADDRESS
}