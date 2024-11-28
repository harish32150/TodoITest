package com.harish.todoitest.domain.entity

import java.util.Date

interface Task {
    val id: Long
    val label: String
    val isCompleted: Boolean
    val isSynced: Boolean
    val createdAt: Date
}