package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.Course
import dev.sanskar.featuretesteduco.core.domain.models.Lesson
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetLessonDetailsUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {

    suspend operator fun invoke( lessonId: String): Resource<Lesson?> {
        return courseRepository.getLessonDetails(lessonId)

    }
}