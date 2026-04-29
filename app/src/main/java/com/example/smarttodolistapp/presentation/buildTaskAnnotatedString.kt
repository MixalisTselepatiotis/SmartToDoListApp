package com.example.smarttodolistapp.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri
import com.example.smarttodolistapp.domain.model.TaskActionType
import com.example.smarttodolistapp.domain.model.TaskItem

@Composable
fun buildTaskAnnotatedString(
    task: TaskItem,
    context: Context
) = buildAnnotatedString {
    val normalColor = if (task.isCompleted) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val normalDecoration = if (task.isCompleted) {
        TextDecoration.LineThrough
    } else {
        TextDecoration.None
    }

    if (task.actions.isEmpty()) {
        withStyle(
            SpanStyle(
                color = normalColor,
                textDecoration = normalDecoration
            )
        ) {
            append(task.text)
        }

        return@buildAnnotatedString
    }

    var currentIndex = 0

    task.actions.sortedBy { it.start }.forEach { action ->
        if (action.start > currentIndex) {
            withStyle(
                SpanStyle(
                    color = normalColor,
                    textDecoration = normalDecoration
                )
            ) {
                append(task.text.substring(currentIndex, action.start))
            }
        }

        val clickedValue = cleanActionValue(
            type = action.type,
            value = action.value
        )

        val linkDecoration = if (task.isCompleted) {
            TextDecoration.LineThrough
        } else {
            TextDecoration.Underline
        }

        withLink(
            LinkAnnotation.Clickable(
                tag = "${action.type.name}|$clickedValue",
                linkInteractionListener = {
                    openTaskAction(
                        context = context,
                        type = action.type,
                        value = clickedValue
                    )
                }
            )
        ) {
            withStyle(
                SpanStyle(
                    color = if (task.isCompleted) {
                        normalColor
                    } else {
                        Color(0xFF1565C0)
                    },
                    textDecoration = linkDecoration
                )
            ) {
                append(task.text.substring(action.start, action.end))
            }
        }

        currentIndex = action.end
    }

    if (currentIndex < task.text.length) {
        withStyle(
            SpanStyle(
                color = normalColor,
                textDecoration = normalDecoration
            )
        ) {
            append(task.text.substring(currentIndex))
        }
    }
}

private fun openTaskAction(
    context: Context,
    type: TaskActionType,
    value: String
) {
    val intent = when (type) {
        TaskActionType.PHONE -> {
            Intent(Intent.ACTION_DIAL, "tel:$value".toUri())
        }

        TaskActionType.EMAIL -> {
            Intent(Intent.ACTION_SENDTO, "mailto:$value".toUri())
        }

        TaskActionType.URL -> {
            val normalized = if (
                value.startsWith("http://") ||
                value.startsWith("https://")
            ) {
                value
            } else {
                "https://$value"
            }

            Intent(Intent.ACTION_VIEW, normalized.toUri())
        }

        TaskActionType.ADDRESS -> {
            Intent(
                Intent.ACTION_VIEW,
                "geo:0,0?q=${Uri.encode(value)}".toUri()
            )
        }
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(
            context,
            "No app found for this action",
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun cleanActionValue(
    type: TaskActionType,
    value: String
): String {
    return when (type) {
        TaskActionType.PHONE -> {
            value.filter { it.isDigit() || it == '+' }
        }

        TaskActionType.EMAIL -> {
            value.trim().trim(',', '.', ';', ':', ')', '(')
        }

        TaskActionType.URL -> {
            value.trim().trim(',', '.', ';', ':', ')', '(')
        }

        TaskActionType.ADDRESS -> {
            value.trim()
        }
    }
}

fun TaskActionType.toUiLabel(): String {
    return when (this) {
        TaskActionType.PHONE -> "Phone"
        TaskActionType.EMAIL -> "Email"
        TaskActionType.URL -> "URL"
        TaskActionType.ADDRESS -> "Address"
    }
}