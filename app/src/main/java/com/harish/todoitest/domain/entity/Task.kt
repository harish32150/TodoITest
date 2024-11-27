package com.harish.todoitest.domain.entity

interface Task {
    val id: Long
    val label: String
    val isCompleted: Boolean
    val isSynced: Boolean
    /*val createdAt: Date*/
}