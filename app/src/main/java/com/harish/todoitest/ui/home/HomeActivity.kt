package com.harish.todoitest.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.harish.todoitest.ui.onboarding.AuthView
import com.harish.todoitest.ui.onboarding.LoginView
import com.harish.todoitest.ui.onboarding.OnboardingNavDestination
import com.harish.todoitest.ui.onboarding.RegisterView
import com.harish.todoitest.ui.onboarding.SplashView
import com.harish.todoitest.ui.theme.TodoITestTheme
import com.harish.todoitest.ui.upsert.TaskUpsertView
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterialNavigationApi::class)
@AndroidEntryPoint
class HomeActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TodoITestTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues = innerPadding)) {
                        HomeNavGraph(bottomSheetNavigator, navController)

                        FloatingActionButton(
                            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
                            onClick = {
                                navController.navigate(HomeNavDestination.TaskUpsert.route)
                            }
                        ) {
                            Icon(imageVector = Icons.TwoTone.Add, contentDescription = "add task")
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialNavigationApi
@Composable
private fun HomeNavGraph(bottomSheetNavigator: BottomSheetNavigator, navController: NavHostController) {
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = HomeNavDestination.TaskList.route
        ) {
            composable(HomeNavDestination.TaskList.route) {
                TaskListView(navController)
            }
            bottomSheet(HomeNavDestination.TaskUpsert.route) {
                TaskUpsertView(navController)
            }
        }
    }
}

internal sealed class HomeNavDestination(val route: String) {
    data object TaskList: HomeNavDestination("task_list")
    data object TaskUpsert: HomeNavDestination("task_upsert")
}