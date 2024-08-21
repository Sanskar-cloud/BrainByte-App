package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.Project
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetProjectsForCourse @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(
        courseId: String
    ): Resource<List<Project>> {
        return courseRepository.getProjectsForCourse(courseId)
    }
}