package dev.sanskar.featuretesteduco.feature_profile.presentation.profile

import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.models.UserItem
import dev.sanskar.featuretesteduco.feature_profile.domain.model.Profile

data class ProfileState(
    val profile: Profile? = null,
    val courses: List<CourseOverview> = emptyList(),
    val userItems: List<UserItem> = emptyList(),
    val isLoadingProfile: Boolean = false,
    val isLoadingCourses: Boolean = false,
    val isLoadingUserItems: Boolean = false,
    val isLogoutDialogVisible: Boolean = false
)