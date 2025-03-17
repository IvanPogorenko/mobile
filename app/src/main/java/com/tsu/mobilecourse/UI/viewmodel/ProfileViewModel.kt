package com.tsu.mobilecourse.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tsu.mobilecourse.data.model.ProfileModel
import com.tsu.mobilecourse.data.model.TestModel
import com.tsu.mobilecourse.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel (
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _bioOverlayVisible = MutableStateFlow(false)
    val bioOverlayVisible: StateFlow<Boolean> = _bioOverlayVisible.asStateFlow()

    private val _interestsOverlayVisible = MutableStateFlow(false)
    val interestsOverlayVisible: StateFlow<Boolean> = _interestsOverlayVisible.asStateFlow()

    private val _infoOverlayVisible = MutableStateFlow(false)
    val infoOverlayVisible: StateFlow<Boolean> = _infoOverlayVisible.asStateFlow()

    private val _additionalBioVisible = MutableStateFlow(false)
    val additionalBioVisible: StateFlow<Boolean> = _additionalBioVisible.asStateFlow()

    private val _additionalTestVisible = MutableStateFlow(false)
    val additionalTestVisible: StateFlow<Boolean> = _additionalTestVisible.asStateFlow()

    private val _selectedInterests = MutableStateFlow<List<String>>(emptyList())
    val selectedInterests: StateFlow<List<String>> = _selectedInterests.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                _uiState.value = ProfileUiState.Loading

                viewModelScope.launch {
                    repository.getProfile().collect { profile ->
                        val currentState = _uiState.value
                        if (currentState is ProfileUiState.Success) {
                            _uiState.value = ProfileUiState.Success(profile, currentState.tests)
                        } else {
                            repository.getTests().collectLatest { tests ->
                                _uiState.value = ProfileUiState.Success(profile, tests)
                            }
                        }
                    }
                }

                viewModelScope.launch {
                    repository.getTests().collect { tests ->
                        val currentState = _uiState.value
                        if (currentState is ProfileUiState.Success) {
                            _uiState.value = ProfileUiState.Success(currentState.profile, tests)
                        }
                    }
                }

            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun toggleBioOverlay() {
        _bioOverlayVisible.update { !it }
    }

    fun toggleInterestsOverlay() {
        _interestsOverlayVisible.update { !it }

        if (_interestsOverlayVisible.value) {
            val currentState = _uiState.value
            if (currentState is ProfileUiState.Success) {
                _selectedInterests.value = currentState.profile.interests
            }
        }
    }

    fun toggleInfoOverlay() {
        _infoOverlayVisible.update { !it }
    }

    fun toggleAdditionalBio() {
        _additionalBioVisible.update { !it }
    }

    fun toggleAdditionalTest() {
        _additionalTestVisible.update { !it }
    }

    fun toggleInterestSelection(interest: String) {
        _selectedInterests.update { currentInterests ->
            if (currentInterests.contains(interest)) {
                currentInterests - interest
            } else {
                currentInterests + interest
            }
        }
    }

    fun updateBio(bio: String) {
        viewModelScope.launch {
            try {
                repository.updateBio(bio)
                toggleBioOverlay()
                loadProfileData()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to update bio")
            }
        }
    }

    fun updateInterests() {
        viewModelScope.launch {
            try {
                repository.updateInterests(_selectedInterests.value)
                toggleInterestsOverlay()
                loadProfileData()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to update interests")
            }
        }
    }

    fun updatePersonalInfo(
        country: String,
        city: String,
        worldview: String,
        education: String,
        languages: List<String>
    ) {
        viewModelScope.launch {
            try {
                repository.updatePersonalInfo(country, city, worldview, education, languages)
                toggleInfoOverlay()
                loadProfileData()
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Failed to update personal info")
            }
        }
    }

    class Factory(private val repository: ProfileRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: ProfileModel, val tests: List<TestModel>) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}