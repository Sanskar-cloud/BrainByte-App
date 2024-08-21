package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class CreateCommentUseCase @Inject constructor(
    private val repository: CourseRepository
) {

    suspend operator fun invoke(courseId: String, comment: String): SimpleResource {
        if(comment.isBlank()) {
            return Resource.Error(UiText.StringResource(R.string.error_field_empty))
        }
        if(courseId.isBlank()) {
            return Resource.Error(UiText.unknownError())
        }
        return repository.createComment(courseId, comment)
    }
}