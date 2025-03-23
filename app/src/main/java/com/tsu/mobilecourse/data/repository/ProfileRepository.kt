package com.tsu.mobilecourse.data.repository

import android.content.Context
import com.tsu.mobilecourse.R
import com.tsu.mobilecourse.data.db.AppDatabase
import com.tsu.mobilecourse.data.db.TestEntity
import com.tsu.mobilecourse.data.model.ProfileModel
import com.tsu.mobilecourse.data.model.TestModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface ProfileRepository {
    fun getProfile(): Flow<ProfileModel>
    suspend fun updateProfile(profile: ProfileModel)

    suspend fun getTests(): List<TestEntity>
//    suspend fun saveTests(tests: List<TestModel>)


    suspend fun updateBio(bio: String)
    suspend fun updateInterests(interests: List<String>)
    suspend fun updatePersonalInfo(
        country: String,
        city: String,
        worldview: String,
        education: String,
        languages: List<String>
    )
}

class FakeProfileRepository(
    private val context: Context
) : ProfileRepository {

    private val database = AppDatabase.getDatabase(context)
    private val testDao = database.testDao()

    private val _profileFlow = MutableStateFlow(
        ProfileModel(
            name = "Александр",
            age = 25,
            city = "",
            country = "",
            worldview = "",
            education = "",
            languages = emptyList(),
            bio = "",
            interests = emptyList(),
            completedProfile = 20
        )
    )

    override fun getProfile(): Flow<ProfileModel> {
        return _profileFlow
    }

    override suspend fun getTests(): List<TestEntity> {
        return testDao.getAllTests()
    }

    override suspend fun updateProfile(profile: ProfileModel) {
        _profileFlow.update { profile }
    }

    override suspend fun updateBio(bio: String) {
        _profileFlow.update { profile ->
            profile.copy(
                bio = bio,
                completedProfile = calculateCompletionPercentage(profile.copy(bio = bio))
            )
        }
    }

    override suspend fun updateInterests(interests: List<String>) {
        _profileFlow.update { profile ->
            profile.copy(
                interests = interests,
                completedProfile = calculateCompletionPercentage(profile.copy(interests = interests))
            )
        }
    }

    override suspend fun updatePersonalInfo(
        country: String,
        city: String,
        worldview: String,
        education: String,
        languages: List<String>
    ) {
        _profileFlow.update { currentProfile ->
            currentProfile.copy(
                country = country,
                city = city,
                worldview = worldview,
                education = education,
                languages = languages,
                completedProfile = calculateCompletionPercentage(
                    currentProfile.copy(
                        country = country,
                        city = city,
                        worldview = worldview,
                        education = education,
                        languages = languages
                    )
                )
            )
        }
    }

    private fun calculateCompletionPercentage(profile: ProfileModel): Int {
        var filledFields = 0
        var totalFields = 0

        if (profile.name.isNotBlank()) filledFields++
        totalFields++

        if (profile.age > 0) filledFields++
        totalFields++

        if (profile.city.isNotBlank()) filledFields++
        totalFields++

        if (profile.country.isNotBlank()) filledFields++
        totalFields++

        if (profile.worldview.isNotBlank()) filledFields++
        totalFields++

        if (profile.education.isNotBlank()) filledFields++
        totalFields++

        if (profile.languages.isNotEmpty()) filledFields++
        totalFields++

        if (profile.bio.isNotBlank()) filledFields++
        totalFields++

        if (profile.interests.isNotEmpty()) filledFields++
        totalFields++

        return (filledFields * 100) / totalFields
    }

}