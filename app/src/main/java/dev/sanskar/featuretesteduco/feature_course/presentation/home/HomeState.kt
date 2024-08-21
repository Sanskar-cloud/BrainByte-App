package dev.sanskar.featuretesteduco.feature_course.presentation.home

import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.models.UserItem
import dev.sanskar.featuretesteduco.feature_profile.domain.model.Profile

data class HomeState(


    val courses: List<CourseOverview> = emptyList(),

    val isLoadingProfile: Boolean = false,
    val isLoadingCourses: Boolean = false,


)