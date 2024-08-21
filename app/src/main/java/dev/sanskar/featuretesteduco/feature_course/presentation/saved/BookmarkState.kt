package dev.sanskar.featuretesteduco.feature_course.presentation.saved

import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview

data class BookmarkState(
    val courses: List<CourseOverview> = emptyList(),
    val isLoading: Boolean = false
)
