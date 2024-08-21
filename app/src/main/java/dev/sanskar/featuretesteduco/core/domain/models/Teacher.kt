package dev.sanskar.featuretesteduco.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val Teacherid:String,
    val username: String,
    val email: String?=null,
    val profileImageUrl: String
)