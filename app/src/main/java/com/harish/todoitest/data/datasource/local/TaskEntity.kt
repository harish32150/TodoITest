package com.harish.todoitest.data.datasource.local

import androidx.room.Entity
import com.harish.todoitest.domain.entity.Task

@Entity(tableName = "tasks")
internal data class TaskEntity(
    override val id: Long,
    override val label: String,
    override val isCompleted: Boolean,
    /* sync params */
    val isNew: Boolean,
    val isUpdated: Boolean,
    val isDeleted: Boolean,
) : Task {
    constructor(from: Task): this(
        id = from.id,
        label = from.label,
        isCompleted = from.isCompleted,
        isNew = false,
        isDeleted = false,
        isUpdated = false
    )
}