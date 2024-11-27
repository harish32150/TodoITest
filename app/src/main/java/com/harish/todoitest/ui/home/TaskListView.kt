package com.harish.todoitest.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.harish.todoitest.domain.entity.Task

@Composable
internal fun TaskListView(navController: NavHostController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val taskListState = viewModel.taskListStateFlow.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val taskList = taskListState.value
        if (taskList.isEmpty()) {
            /* todo - empty state */
        } else items(
            items = taskList,
            itemContent = { task -> TaskItemView(task) }
        )
    }
}

@Composable
private fun TaskItemView(task: Task) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = task.isCompleted, onCheckedChange = {
                /*todo*/
            })

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                text = task.label,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis
            )

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = "More options"
                    )
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = {
                        expanded = false/*onEditTask(task)*/
                    })

                    DropdownMenuItem(text = { Text("Delete") }, onClick = {
                        expanded = false/*on delete(task)*/
                    })
                }
            }
        }
    }
}