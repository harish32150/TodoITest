package com.harish.todoitest.ui.onboarding

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.harish.todoitest.ui.theme.TodoITestTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialNavigationApi
@AndroidEntryPoint
class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoITestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OnboardingNavGraph(innerPadding = innerPadding)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoITestTheme {
        Greeting("Android")
    }
}

@ExperimentalMaterialNavigationApi
@Composable
private fun OnboardingNavGraph(innerPadding: PaddingValues) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = OnboardingNavDestination.Splash.route
        ) {
            composable(OnboardingNavDestination.Splash.route) {
                SplashView(navController)
            }
            composable(OnboardingNavDestination.Authentication.route) {
                AuthView(navController)
            }
            bottomSheet(OnboardingNavDestination.Login.route) {
                LoginView(navController)
            }
            bottomSheet(OnboardingNavDestination.Register.route) {
                RegisterView(navController)
            }
        }
    }
}

internal sealed class OnboardingNavDestination(val route: String) {
    data object Splash: OnboardingNavDestination("splash")
    data object Authentication: OnboardingNavDestination("auth")
    data object Login: OnboardingNavDestination("login")
    data object Register: OnboardingNavDestination("register")
}