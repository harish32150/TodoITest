package com.harish.todoitest.data.datasource.remote

import com.harish.todoitest.domain.entity.Task
import com.squareup.moshi.Json

internal data class TaskApiModel(
    override val id: Long,
    override val label: String,
    @Json(name = "completed") override val isCompleted: Boolean
) : Task {
    constructor(from: Task): this(
        id = from.id,
        label = from.label,
        isCompleted = from.isCompleted
    )
}