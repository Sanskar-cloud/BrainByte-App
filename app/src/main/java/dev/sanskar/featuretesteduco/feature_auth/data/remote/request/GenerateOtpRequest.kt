package dev.sanskar.featuretesteduco.feature_auth.data.remote.request

@kotlinx.serialization.Serializable
data class GenerateOtpRequest(
    val email: String,

)