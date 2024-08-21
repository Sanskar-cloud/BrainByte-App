package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import dev.sanskar.featuretesteduco.core.domain.models.CourseResource
import dev.sanskar.featuretesteduco.core.domain.models.Lesson
import dev.sanskar.featuretesteduco.core.util.Resource

data class LessonDetailState(
    val lesson: Lesson? = null,
    val isLoadingLesson: Boolean = false,
)

data class ResourcesState(
    val resources: List<CourseResource> = emptyList(),
    val isLoading: Boolean = false
)

