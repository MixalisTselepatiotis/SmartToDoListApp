package com.example.smarttodolistapp.data.mapper

import com.example.smarttodolistapp.data.local.TaskEntity
import com.example.smarttodolistapp.domain.model.TaskAction
import com.example.smarttodolistapp.domain.model.TaskActionType
import com.example.smarttodolistapp.domain.model.TaskItem

fun TaskEntity.toDomain(): TaskItem {
    return TaskItem(
        id = id,
        text = text,
        isCompleted = isCompleted,
        actions = actionsText.toTaskActions(),
        createdAt = createdAt
    )
}

fun TaskItem.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        text = text,
        isCompleted = isCompleted,
        actionsText = actions.toActionsText(),
        createdAt = createdAt
    )
}



fun List<TaskAction>.toActionsText(): String {
    return joinToString(separator = ",") { action ->
        "${action.type.name}|${action.start}|${action.end}|${action.value}"
    }
}

fun String.toTaskActions(): List<TaskAction> {
    if (isBlank()) return emptyList()

    return split(",")
        .mapNotNull { actionText ->
            val parts = actionText.split("|")

            if (parts.size < 4) return@mapNotNull null

            try {
                TaskAction(
                    type = TaskActionType.valueOf(parts[0]),
                    start = parts[1].toInt(),
                    end = parts[2].toInt(),
                    value = parts[3]
                )
            } catch (e: Exception) {
                null
            }
        }
}