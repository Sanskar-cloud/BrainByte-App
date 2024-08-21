package dev.sanskar.featuretesteduco.feature_auth.domain.models

import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_auth.presentation.util.AuthError

data class SignInResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)