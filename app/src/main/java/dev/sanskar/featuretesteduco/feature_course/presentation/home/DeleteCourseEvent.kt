package dev.sanskar.featuretesteduco.feature_course.presentation.home

sealed class DeleteCourseEvent {
    data class DeleteCourse(val courseId: String) : DeleteCourseEvent()
}