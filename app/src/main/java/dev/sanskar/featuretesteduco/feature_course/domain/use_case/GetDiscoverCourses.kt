package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.FilterResult
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetDiscoverCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {

    suspend operator fun invoke(
        filterResult: FilterResult
    ) = courseRepository.getDiscoverCourses(filterResult)

}