package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class ToggleLikeForParentUseCase @Inject constructor(
    private val repository: CourseRepository
) {

    suspend operator fun invoke(
        parentId: String,
        parentType: Int,
        userId: String,
        isLiked: Boolean
    ): SimpleResource {
        return if(isLiked) {
            repository.unlikeParent(parentId, parentType)
        } else {
            repository.likeParent(parentId, parentType, userId )
        }
    }
}