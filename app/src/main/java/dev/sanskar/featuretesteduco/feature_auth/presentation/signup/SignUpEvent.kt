package dev.sanskar.featuretesteduco.feature_auth.presentation.signup

import android.content.Context
import dev.sanskar.featuretesteduco.feature_auth.presentation.signin.SignInEvent

sealed class SignUpEvent {
    data class EnteredUsername(val value: String): SignUpEvent()
    data class EnteredEmail(val value: String): SignUpEvent()
    data class EnteredUserType(val value: String): SignUpEvent()
    data class EnteredPassword(val value: String): SignUpEvent()
    data class EnteredAccountNumber(val value:String):SignUpEvent()
    data class EnteredIfscCode(val value:String):SignUpEvent()
    data class EnteredAccountHolderName(val value: String):SignUpEvent()
    data class EnteredOtp(val value: String): SignUpEvent()
    object TogglePasswordVisibility : SignUpEvent()
    object SignUp : SignUpEvent()

    data class OnSignInWithGoogleClicked(val activituContext: Context):SignUpEvent()
}