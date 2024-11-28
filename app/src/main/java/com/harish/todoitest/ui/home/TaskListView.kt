package com.harish.todoitest.ui.home

import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harish.todoitest.R
import com.harish.todoitest.domain.entity.Task
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
internal fun TaskListView(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val context = LocalContext.current
    val taskListState = viewModel.taskListStateFlow.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Hello ${viewModel.username},", style = MaterialTheme.typography.titleLarge)

                Text(
                    if (taskListState.value.count { it.isCompleted.not() } == 0) "All tasks completed"
                    else "You have ${taskListState.value.count { it.isCompleted.not() }} pending tasks",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            IconButton(onClick = { viewModel.checkCanLogout() }) {
                Icon(
                    painterResource(R.drawable.logout_24dp_e8eaed_fill0_wght400_grad0_opsz24),
                    "logout"
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            val taskList = taskListState.value
            if (taskList.isEmpty()) item {
              Column(
                  modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 48.dp),
                  verticalArrangement = Arrangement.SpaceAround,
                  horizontalAlignment = Alignment.CenterHorizontally
              ) {
                  Text("No Tasks, start by creating your first.", style = MaterialTheme.typography.bodyLarge)
                  ElevatedButton(
                      modifier = Modifier.padding(top = 12.dp),
                      onClick = { navController.navigate(HomeNavDestination.TaskUpsert().route) }
                  ) { Text("Create Task") }
              }
            } else items(
                items = taskList,
                itemContent = { task ->
                    TaskItemView(
                        task,
                        markCompleted = viewModel::markCompleted,
                        onUpdate = { navController.navigate(HomeNavDestination.TaskUpsert(it.id).route) },
                        onDelete = { viewModel.deleteTask(it.id) }
                    )
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        launch {
            viewModel.checkCanLogoutFlow.collectLatest {
                if (it) onLogout()
                else AlertDialog.Builder(context)
                    .setPositiveButton("Cancel", null)
                    .setNegativeButton("Confirm", { _dialog, _ -> onLogout(); _dialog.dismiss() })
                    .setTitle("Are you sure?")
                    .setMessage("You have unsynced tasks. Logging out now will result in losing these tasks. Are you sure you want to proceed?")
                    .show()
            }
        }
    }
}

@Composable
private fun TaskItemView(
    task: Task,
    markCompleted: (Long, Boolean) -> Unit,
    onUpdate: (Task) -> Unit,
    onDelete: (Task) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = task.isCompleted, onCheckedChange = {
                markCompleted(task.id, it)
            })

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = task.label,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (task.isSynced) Color.Green else Color.Red,
                                shape = CircleShape
                            )
                    )

                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = SimpleDateFormat("dd MMM yyyy | HH:mm:ss", Locale.getDefault()).format(task.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Unspecified.copy(alpha = 0.5f)
                    )
                }
            }

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "More options"
                    )
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = {
                        onUpdate.invoke(task)
                        expanded = false
                    })

                    DropdownMenuItem(text = { Text("Delete") }, onClick = {
                        onDelete.invoke(task)
                        expanded = false
                    })
                }
            }
        }
    }
}