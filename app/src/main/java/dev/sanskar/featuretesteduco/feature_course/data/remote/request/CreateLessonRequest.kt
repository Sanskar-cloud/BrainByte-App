package dev.sanskar.featuretesteduco.feature_course.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
 data class CreateLessonRequest(
  val lessonNo: String,
  val name: String,
  val desc: String,
){
}