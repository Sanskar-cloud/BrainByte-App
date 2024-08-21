package dev.sanskar.featuretesteduco.feature_settings.presentation

import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_profile.domain.model.UpdateProfileData

data class CreateCourseState(
    val createCourseData: CreateCourseRequest? = null,
    val isLoading: Boolean = false
)
data class CreateCourseResourceState(
    val isLoading: Boolean = false,
)