package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import android.content.Context
import android.net.Uri
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_course.data.remote.request.CreateCourseRequest
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class CreateCourseResourceUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(
        courseId: String,

        thumbnailuri: List<Uri>?,

        context: Context
    ): SimpleResource {
        return courseRepository.createCourseResource(
           courseId = courseId,
            thumbnailuri,

            context
        )
    }}