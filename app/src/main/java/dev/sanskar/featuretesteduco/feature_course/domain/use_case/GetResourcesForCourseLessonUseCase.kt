package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.CourseResource
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetResourcesForCourseLessonUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(
                lessonId:String
    ): Resource<List<CourseResource>> {
        return repository.getResourcesForCourseLesson(lessonId)
    }
}