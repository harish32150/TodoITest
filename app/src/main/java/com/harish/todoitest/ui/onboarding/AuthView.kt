package com.harish.todoitest.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun AuthView() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {  }

            ElevatedButton(onClick = {}) {
                Text("Login")
            }
            ElevatedButton(onClick = {}) {
                Text("Register")
            }
        }
    }
}