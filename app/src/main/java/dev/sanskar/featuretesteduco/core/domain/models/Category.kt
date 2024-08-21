package dev.sanskar.featuretesteduco.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val caid: String,
    val categoryImageUrl: String,
    val categoryName: String
)
