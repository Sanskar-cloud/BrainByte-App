package dev.sanskar.featuretesteduco.feature_auth.presentation.util

import dev.sanskar.featuretesteduco.core.util.Error

sealed class AuthError : Error() {
    object FieldEmpty : AuthError()
    object InputTooShort : AuthError()
    object InvalidEmail: AuthError()
    object InvalidPassword : AuthError()
    object InvalidaccountType : AuthError()
    object MustMatchBothPassword: AuthError()
}