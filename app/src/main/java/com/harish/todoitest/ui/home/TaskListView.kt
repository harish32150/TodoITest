package com.harish.todoitest.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
internal fun TaskListView(navController: NavHostController) {
    val viewModel = hiltViewModel<HomeViewModel>()

}