package com.tsu.mobilecourse.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val result: String,
    val isCompleted: Boolean
)