package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.Course
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class SearchCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(
        query: String,
        page: Int
    ): Resource<List<CourseOverview>> {
        return courseRepository.searchCourses(query, page)
    }
}