package com.harish.todoitest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.todoitest.data.AppPrefs
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.get
import com.harish.todoitest.domain.onSuccess
import com.harish.todoitest.domain.repository.TaskRepository
import com.harish.todoitest.domain.usecase.SyncAllServerTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val syncAllServerTaskUseCase: SyncAllServerTaskUseCase,
    private val prefs: AppPrefs
) : ViewModel() {

    val username: String?
        get() = prefs.username

    private val _syncAllServerTaskResultFlow = MutableSharedFlow<KResult<Unit>>()
    val syncAllServerTaskResultFlow = _syncAllServerTaskResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    init {
        viewModelScope.launch {
            _syncAllServerTaskResultFlow.emit(KResult.Loading)
            _syncAllServerTaskResultFlow.emit(syncAllServerTaskUseCase.invoke())
        }
    }

    val taskListStateFlow = taskRepository.streamAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun markCompleted(id: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            /* result ignored, should be always success */
            taskRepository.markCompleted(id, isCompleted)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            taskRepository.delete(id)
        }
    }

    private val _checkCanLogoutFlow = MutableSharedFlow<Boolean>()
    val checkCanLogoutFlow = _checkCanLogoutFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun checkCanLogout() {
        viewModelScope.launch {
            taskRepository.syncPendingList()
                .onSuccess { _checkCanLogoutFlow.emit(it.isEmpty()) }
        }
    }
}