package com.harish.todoitest.data.datasource.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harish.todoitest.domain.entity.Task

@Entity(tableName = "tasks")
internal data class TaskEntity(
    @PrimaryKey override val id: Long,
    override val label: String,
    override val isCompleted: Boolean,
    /* sync params */
    val isNew: Boolean,
    val isUpdated: Boolean,
    val isDeleted: Boolean,
) : Task {
    constructor(from: Task, isNew: Boolean = false, isUpdated: Boolean = false, isDeleted: Boolean = false): this(
        id = from.id,
        label = from.label,
        isCompleted = from.isCompleted,
        isNew = isNew,
        isDeleted = isUpdated,
        isUpdated = isDeleted
    )

    constructor(label: String): this(
        id = 0,
        label = label,
        isCompleted = false,
        isNew = true,
        isUpdated = false,
        isDeleted = false
    )
}