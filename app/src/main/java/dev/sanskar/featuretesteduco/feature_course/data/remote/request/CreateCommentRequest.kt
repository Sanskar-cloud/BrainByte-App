package dev.sanskar.featuretesteduco.feature_course.data.remote.request

@kotlinx.serialization.Serializable
data class CreateCommentRequest(
    val comment: String,
    val courseId: String,
)