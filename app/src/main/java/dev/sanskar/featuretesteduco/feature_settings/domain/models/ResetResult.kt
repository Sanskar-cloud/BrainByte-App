package dev.sanskar.featuretesteduco.feature_settings.domain.models

import dev.sanskar.featuretesteduco.core.util.SimpleResource
import dev.sanskar.featuretesteduco.feature_auth.presentation.util.AuthError

data class ResetResult(
    val oldPasswordError: AuthError? = null,
    val newPasswordError: AuthError? = null,
    val confirmPasswordError: AuthError? = null,
    val result: SimpleResource? = null
)

