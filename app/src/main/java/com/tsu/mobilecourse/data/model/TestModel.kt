package com.tsu.mobilecourse.data.model

data class TestModel(
    val id: Int,
    val name: String,
    val shortDescription: String,
    val fullDescription: String,
    val imageResId: Int,
    val result: String? = null,
    val isCompleted: Boolean = false
)
