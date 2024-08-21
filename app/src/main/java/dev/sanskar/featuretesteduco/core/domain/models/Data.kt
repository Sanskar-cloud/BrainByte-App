package dev.sanskar.featuretesteduco.core.domain.models

data class Data(
    val city: String,
    val country: String,
    val populationCounts: List<PopulationCount>
)