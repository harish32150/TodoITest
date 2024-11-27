package com.harish.todoitest.ui.upsert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.repository.TaskRepository
import com.harish.todoitest.domain.usecase.SyncTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskUpsertViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val syncUseCase: SyncTaskUseCase
): ViewModel() {

    private val _createTaskResultFlow = MutableSharedFlow<KResult<Long>>()
    val createTaskResultFlow = _createTaskResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun createTask(label: String) {
        viewModelScope.launch {
            _createTaskResultFlow.emit(KResult.Loading)
            _createTaskResultFlow.emit(taskRepository.create(label).onSuccess { syncUseCase.invoke(it) })
        }
    }
}