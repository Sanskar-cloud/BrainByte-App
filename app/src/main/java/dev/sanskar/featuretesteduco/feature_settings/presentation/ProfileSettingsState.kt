package dev.sanskar.featuretesteduco.feature_settings.presentation

import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData

data class ProfileSettingsState(
    val profileSettingsData: UpdateProfileData? = null,

    val isLoading: Boolean = false,
)
