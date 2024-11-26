package com.harish.todoitest.domain.entity

interface Task {
    val id: Long
    val label: String
    val isCompleted: Boolean
    /*val createdAt: Date*/
}