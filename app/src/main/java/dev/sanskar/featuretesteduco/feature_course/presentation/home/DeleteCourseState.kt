package dev.sanskar.featuretesteduco.feature_course.presentation.home

data class DeleteCourseState(
    val isLoading: Boolean = false,
    val courseId: String? = null
)