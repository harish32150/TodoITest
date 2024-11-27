package com.harish.todoitest.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harish.todoitest.domain.KResult
import com.harish.todoitest.domain.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    val isAuthenticatedFlow = flow<Boolean> {
        delay(1500)
        emit(authenticationRepository.isAuthenticated())
    }

    private val _registerResultFlow: MutableSharedFlow<KResult<Long>> = MutableSharedFlow()
    val registerResultFlow: SharedFlow<KResult<Long>> = _registerResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun register(username: String, password: String) {
        viewModelScope.launch {
            _registerResultFlow.emit(KResult.Loading)
            authenticationRepository.register(username, password)
                .also { _registerResultFlow.emit(it) }
        }
    }

    private val _loginResultFlow: MutableSharedFlow<KResult<String>> = MutableSharedFlow()
    val loginResultFlow: SharedFlow<KResult<String>> = _loginResultFlow.shareIn(viewModelScope, SharingStarted.Lazily)
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginResultFlow.emit(KResult.Loading)
            authenticationRepository.login(username, password)
                .also { _loginResultFlow.emit(it) }
        }
    }
}