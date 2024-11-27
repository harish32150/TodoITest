package com.harish.todoitest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.todoitest.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    taskRepository: TaskRepository,
) : ViewModel() {
    val taskListStateFlow = taskRepository.streamAll()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}