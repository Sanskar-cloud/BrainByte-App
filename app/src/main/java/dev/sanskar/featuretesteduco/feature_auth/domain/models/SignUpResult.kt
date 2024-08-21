package dev.sanskar.featuretesteduco.feature_auth.domain.models

import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_auth.presentation.util.AuthError

data class SignUpResult(
    val emailError: AuthError? = null,
    val usernameError: AuthError? = null,
    val passwordError: AuthError? = null,
    val accountTypeError: AuthError? = null,
    val otpError: AuthError? = null,
    val accountHolderNameError: String? = null,
    val accountNumberError: String? = null,
    val ifscCodeError: String? = null,
    val result: SimpleResource? = null
)