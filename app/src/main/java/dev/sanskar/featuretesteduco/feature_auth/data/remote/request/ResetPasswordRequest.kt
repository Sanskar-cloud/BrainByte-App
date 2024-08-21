package dev.sanskar.featuretesteduco.feature_auth.data.remote.request

@kotlinx.serialization.Serializable
data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
