package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.CourseResource
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetResourcesForCourse @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(
        courseId: String
    ): Resource<List<CourseResource>> {
        return repository.getResourcesForCourse(courseId)
    }
}