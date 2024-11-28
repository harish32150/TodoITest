package com.harish.todoitest.ui.upsert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.entity.Task
import com.harish.todoitest.domain.getOrNull
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.repository.TaskRepository
import com.harish.todoitest.domain.usecase.SyncTaskUseCase
import com.harish.todoitest.domain.usecase.UpdateTaskLabelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskUpsertViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val updateTaskLabelUseCase: UpdateTaskLabelUseCase
): ViewModel() {

    private val _createTaskResultFlow = MutableSharedFlow<KResult<Long>>()
    val createTaskResultFlow = _createTaskResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun createTask(label: String) {
        viewModelScope.launch {
            _createTaskResultFlow.emit(KResult.Loading)
            _createTaskResultFlow.emit(taskRepository.create(label))
        }
    }

    private val _editableTaskResultFlow = MutableStateFlow<KResult<Task>>(KResult.Loading)
    val editableTaskResultFlow = _editableTaskResultFlow.stateIn(viewModelScope, SharingStarted.Lazily, KResult.Loading)
    fun fetch(id: Long) {
        viewModelScope.launch {
            _editableTaskResultFlow.emit(KResult.Loading)
            _editableTaskResultFlow.emit(taskRepository.get(id))
        }
    }

    private val _updateTaskResultFlow = MutableSharedFlow<KResult<Unit>>()
    val updateTaskResultFlow = _updateTaskResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun updateTask(id: Long, label: String) {
        viewModelScope.launch {
            _updateTaskResultFlow.emit(KResult.Loading)
            _updateTaskResultFlow.emit(updateTaskLabelUseCase.invoke(id, label))
        }
    }
}