package dev.sanskar.featuretesteduco.feature_course.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.models.Comment
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.feature_course.domain.repository.CourseRepository
import javax.inject.Inject

class GetCommentsForCourseUseCase @Inject constructor(
    private val repository: CourseRepository
) {

    suspend operator fun invoke(courseId: String): Resource<List<Comment>> {
        return repository.getCommentsForCourse(courseId)
    }
}