package dev.sanskar.featuretesteduco.core.domain.states

import dev.sanskar.featuretesteduco.core.util.Error
data class StandardTextFieldState(
    val text: String = "",
    val error: Error? = null
)
data class StandardTextFieldState2(
    val text: String = "",
    val error: String? = null
)