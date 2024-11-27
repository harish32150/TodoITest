package com.harish.todoitest.data

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPrefs @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPrefs by lazy { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    var accessToken: String? = null
        get() = sharedPrefs.getString("access_token", null)
        set(value) {
            sharedPrefs.edit { putString("access_token", value) }
            field = value
        }
}