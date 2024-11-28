package com.harish.todoitest.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.harish.todoitest.domain.handle
import com.harish.todoitest.domain.usecase.LogoutUseCase
import com.harish.todoitest.ui.onboarding.OnboardingActivity
import com.harish.todoitest.ui.theme.TodoITestTheme
import com.harish.todoitest.ui.toast
import com.harish.todoitest.ui.upsert.TaskUpsertView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterialNavigationApi::class)
@AndroidEntryPoint
class HomeActivity: ComponentActivity() {

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TodoITestTheme {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)) {
                        HomeNavGraph(bottomSheetNavigator, navController, ::handleLogoutAction)

                        FloatingActionButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(24.dp),
                            onClick = {
                                navController.navigate(HomeNavDestination.TaskUpsert().route)
                            }
                        ) {
                            Icon(imageVector = Icons.TwoTone.Add, contentDescription = "add task")
                        }
                    }
                }
            }
        }
    }

    private fun handleLogoutAction() {
        lifecycleScope.launch {
            logoutUseCase.invoke()
                .handle(
                    onSuccess = {
                        startActivity(Intent(this@HomeActivity, OnboardingActivity::class.java))
                        finish()
                    },
                    onError = { toast(it.message ?: it.toString()) }
                )
        }
    }
}

@ExperimentalMaterialNavigationApi
@Composable
private fun HomeNavGraph(
    bottomSheetNavigator: BottomSheetNavigator,
    navController: NavHostController,
    onLogout: () -> Unit
) {
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            navController = navController,
            startDestination = HomeNavDestination.TaskList.route
        ) {
            composable(HomeNavDestination.TaskList.route) {
                TaskListView(navController, onLogout)
            }
            bottomSheet(
                route = HomeNavDestination.TaskUpsert.run { "$BaseRoute?$TaskIdParam={$TaskIdParam}" },
                arguments = listOf(navArgument(HomeNavDestination.TaskUpsert.TaskIdParam) {
                    type = NavType.LongType
                    nullable = false
                    defaultValue = -1
                })
            ) { navBackStackEntry ->
                TaskUpsertView(
                    navController,
                    taskId = navBackStackEntry.arguments
                        ?.getLong(HomeNavDestination.TaskUpsert.TaskIdParam)
                        ?.takeIf { it >= 0 }
                )
            }
        }
    }
}

internal sealed class HomeNavDestination(val route: String) {
    data object TaskList: HomeNavDestination("task_list")
    data class TaskUpsert(val id: Long = -1): HomeNavDestination("$BaseRoute?$TaskIdParam=$id") {
        companion object {
            const val BaseRoute = "task_upsert"
            const val TaskIdParam = "task_id"
        }
    }
}