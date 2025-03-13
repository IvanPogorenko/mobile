package com.tsu.mobilecourse.data.model

data class ProfileModel (
    val name: String = "",
    val age: Int = 0,
    val city: String = "",
    val country: String = "",
    val worldview: String = "",
    val education: String = "",
    val languages: List<String> = emptyList(),
    val bio: String = "",
    val interests: List<String> = emptyList(),
    val completedProfile: Int = 0
)