package dev.sanskar.featuretesteduco.core.domain.models


@kotlinx.serialization.Serializable
data class CourseOverview(
    val courseId: String,
    val courseName: String,
    val courseTeacherName: String,
    val price:String?=null,
//
    val courseThumbnailUrl: String,
    val courseIntroVideoUrl: String?=null,
    val rating: Double=0.0,
    val noOfStudentRated: Int? = null,
    val noOfStudentEnrolled: Int? = null,
    val tag: String? = null
)
