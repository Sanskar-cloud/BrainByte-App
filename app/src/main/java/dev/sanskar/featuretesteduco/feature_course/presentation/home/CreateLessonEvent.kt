package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.Context
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateLessonRequest

sealed class CreateLessonEvent {
    data class EnteredLessonNo(val lessonNo: String): CreateLessonEvent()
    data class EnteredLessonName(val name: String): CreateLessonEvent()
    data class EnteredDescription(val desc: String): CreateLessonEvent()

    object Cancel: CreateLessonEvent()
    data class SaveLesson(val context: Context, val courseId: String) : CreateLessonEvent()
    data class UploadResource(val context: Context, val courseId: String) : CreateLessonEvent()
    data class DeleteCourse(val courseId: String) : CreateLessonEvent()
}
