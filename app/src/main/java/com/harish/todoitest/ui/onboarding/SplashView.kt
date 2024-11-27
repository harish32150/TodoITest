package com.harish.todoitest.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SplashView(navController: NavHostController, navigateToHome: () -> Unit) {
    val viewModel = hiltViewModel<OnboardingViewModel>()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }

    LaunchedEffect(Unit) {
        viewModel.isAuthenticatedFlow.collectLatest {
            if (it) navigateToHome.invoke()
            else navController.navigate(OnboardingNavDestination.Authentication.route)
        }
    }
}