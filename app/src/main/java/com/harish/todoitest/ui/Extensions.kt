package com.harish.todoitest.ui

import android.content.Context
import android.widget.Toast

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, length).show()
}