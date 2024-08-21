package dev.sanskar.featuretesteduco.core.domain.models

@kotlinx.serialization.Serializable
data class CourseResource(
    val _id: String,
    val courseId: String?=null,
    val lessonId:String?=null,
    val resourceUrl: List<String>,
    val resourceName: List<String>,
    val fileType: List<String>,
    val resourceSize: List<Double>
)
