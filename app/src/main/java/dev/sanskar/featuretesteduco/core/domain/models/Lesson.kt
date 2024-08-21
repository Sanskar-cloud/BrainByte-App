package dev.sanskar.featuretesteduco.core.domain.models

import dagger.Provides

@kotlinx.serialization.Serializable

data class Lesson(
    val _id: String,
    val courseId: String,
    val lessonNo: Int,
    val name: String,
    val thumbnail: String,
    val desc: String,
    val lessonVideoUrl: String
)
