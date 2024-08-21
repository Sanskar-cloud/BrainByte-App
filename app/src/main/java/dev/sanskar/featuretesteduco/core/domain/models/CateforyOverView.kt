package dev.sanskar.featuretesteduco.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CateforyOverView(
    val categoryId:String,
    val categoryImageUrl: String,
    val categoryName: String

) {
}