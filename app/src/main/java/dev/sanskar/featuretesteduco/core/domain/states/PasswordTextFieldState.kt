package dev.sanskar.featuretesteduco.core.domain.states

import dev.sanskar.featuretesteduco.core.util.Error

data class PasswordTextFieldState(
    val text: String = "",
    val error: Error? = null,
    val isPasswordVisible: Boolean = false
)