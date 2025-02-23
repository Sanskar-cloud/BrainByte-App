package dev.sanskar.featuretesteduco.core.presentation.util

import dev.sanskar.featuretesteduco.core.util.Event
import dev.sanskar.featuretesteduco.core.util.UiText

sealed class UiEvent: Event() {
    data class ShowSnackBar(val uiText: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    object NavigateUp : UiEvent()
    object OnSignIn: UiEvent()
    object OnSignOut: UiEvent()
    object OnResetPassword: UiEvent()
}