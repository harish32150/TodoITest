package com.harish.todoitest.ui.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harish.todoitest.domain.handle
import com.harish.todoitest.ui.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskUpsertView(navController: NavHostController, taskId: Long? = null) {
    val viewModel = hiltViewModel<TaskUpsertViewModel>()
    val context = LocalContext.current

    var label by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = if (taskId == null) "Create Task" else "Update Task",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = label,
                onValueChange = { label = it },
                label = { Text("Enter task here") },
                singleLine = false,
                minLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        label.isEmpty() -> "Task is required"
                        else -> null
                    }.also {
                        when {
                            it != null -> context.toast(it)
                            taskId != null -> viewModel.updateTask(taskId, label)
                            else -> viewModel.createTask(label)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(if (taskId == null) "Create" else "Save")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        taskId?.also { viewModel.fetch(it) }

        launch {
            viewModel.createTaskResultFlow.collectLatest { result ->
                result.handle(
                    onSuccess = {
                        context.toast("Task Created")
                        navController.popBackStack()
                    },
                    onLoading = { isLoading = it },
                    onError = { context.toast(it.message ?: it.toString()) }
                )
            }
        }

        if (taskId != null) {
            launch {
                viewModel.editableTaskResultFlow.collectLatest { result ->
                    result.handle(
                        onSuccess = { label = it.label },
                        onLoading = { isLoading = it },
                        onError = { context.toast(it.message ?: it.toString()) }
                    )
                }
            }
            launch {
                viewModel.updateTaskResultFlow.collectLatest { result ->
                    result.handle(
                        onSuccess = {
                            context.toast("Task Saved")
                            navController.popBackStack()
                        },
                        onLoading = { isLoading = it },
                        onError = { context.toast(it.message ?: it.toString()) }
                    )
                }
            }
        }
    }
}