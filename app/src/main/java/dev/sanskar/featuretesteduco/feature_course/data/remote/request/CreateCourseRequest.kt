package dev.sanskar.featuretesteduco.feature_course.data.remote.request

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class CreateCourseRequest(
    val courseTitle: String,
    val description: String,
    val tag: String? = null,
    val price:String?=null,

    val duration: String?=null
)