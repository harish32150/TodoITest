package com.harish.todoitest.data.datasource.remote

import com.harish.todoitest.domain.entity.Task
import com.squareup.moshi.Json
import java.util.Date

internal data class TaskApiModel(
    override val id: Long,
    override val label: String,
    @Json(name = "completed") override val isCompleted: Boolean,
    @Json(name = "created_at") override val createdAt: Date
) : Task {
    constructor(from: Task): this(
        id = from.id,
        label = from.label,
        isCompleted = from.isCompleted,
        createdAt = from.createdAt
    )

    override val isSynced: Boolean
        get() = true
}