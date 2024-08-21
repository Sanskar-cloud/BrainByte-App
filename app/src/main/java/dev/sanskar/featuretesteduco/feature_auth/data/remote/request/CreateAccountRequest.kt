package dev.sanskar.featuretesteduco.feature_auth.data.remote.request

@kotlinx.serialization.Serializable
data class CreateAccountRequest(
    val email: String,
    val username: String,
    val password: String,
    val accountType: String,
    val accountDetails: AccountDetails? = null
)
@kotlinx.serialization.Serializable

data class AccountDetails(
    val accountNumber: String,
    val ifscCode: String,
    val accountHolderName: String


)