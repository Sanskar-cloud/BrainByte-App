package dev.sanskar.featuretesteduco.feature_course.presentation.search.use_cases

import dev.sanskar.featuretesteduco.core.domain.models.Course
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class SearchCourseUseCase2 @Inject constructor(
    private val courseRepository: CourseRepository
)  {
    suspend operator fun invoke(
        query: String,
        page: Int=0,
    ): List<Course>? {
        return courseRepository.searchCourses2(query,page)
    }
}