package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.Context
import dev.sanskar.featuretesteduco.core.domain.models.Duration
import dev.sanskar.featuretesteduco.feature_settings.presentation.SettingsEvent

sealed class CreateCourseEvent {
    data class EnteredCourseTitle(val title: String): CreateCourseEvent()
    data class EnteredDescription(val description: String): CreateCourseEvent()
    data class EnteredTag(val tag: String): CreateCourseEvent()
    data class EnteredPrice(val price: String): CreateCourseEvent()
    data class EnteredDuration(val duration: String): CreateCourseEvent()
    object Cancel: CreateCourseEvent()
    data class SaveCourse(val context: Context) : CreateCourseEvent()
    data class DeleteCourse(val courseId: String) : CreateCourseEvent()

}