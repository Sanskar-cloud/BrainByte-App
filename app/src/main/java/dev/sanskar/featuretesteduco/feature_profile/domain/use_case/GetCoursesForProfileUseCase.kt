package dev.sanskar.featuretesteduco.feature_profile.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.repository.ProfileRepository
import dev.sanskar.featuretesteduco.core.util.Resource

class GetCoursesForProfileUseCase(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(userId: String, page: Int, pageSize: Int): Resource<List<CourseOverview>> {
        return repository.getCoursesPaged(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }
}