package dev.sanskar.featuretesteduco.core.domain.models

data class Red(
    val `data`: List<Data>,
    val error: Boolean,
    val msg: String
)