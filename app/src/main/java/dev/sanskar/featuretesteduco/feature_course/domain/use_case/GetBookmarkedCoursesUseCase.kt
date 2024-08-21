package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetBookmarkedCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(
        userId: String,
        page: Int,
        pageSize: Int= Constants.DEFAULT_PAGE_SIZE
    ): Resource<List<CourseOverview>> {
        return courseRepository.getBookmarkedCourses(userId = userId, page = page, pageSize = pageSize)
    }
}