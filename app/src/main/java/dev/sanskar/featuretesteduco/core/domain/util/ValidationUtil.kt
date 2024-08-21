package dev.sanskar.featuretesteduco.core.domain.util

import android.util.Patterns
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.feature_auth.presentation.util.AuthError

object ValidationUtil {

    fun validateEmail(email: String): AuthError? {
        val trimmedEmail = email.trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AuthError.InvalidEmail
        }
        if(trimmedEmail.isBlank()) {
            return AuthError.FieldEmpty
        }
        return null
    }

    fun validateUsername(username: String): AuthError? {
        val trimmedUsername = username.trim()
        if(trimmedUsername.length < Constants.MIN_USERNAME_LENGTH) {
            return AuthError.InputTooShort
        }
        if(trimmedUsername.isBlank()) {
            return AuthError.FieldEmpty
        }
        return null
    }

    fun validatePassword(password: String): AuthError? {
        val capitalLettersInPassword = password.any { it.isUpperCase() }
        val numberInPassword = password.any { it.isDigit() }
        if(!capitalLettersInPassword || !numberInPassword) {
            return AuthError.InvalidPassword
        }
        if(password.length < Constants.MIN_PASSWORD_LENGTH) {
            return AuthError.InputTooShort
        }
        if(password.isBlank()) {
            return AuthError.FieldEmpty
        }
        return null
    }
    fun validateAccountType(accountType: String): AuthError? {
        val capitalLettersInPassword = accountType.any { it.isUpperCase() }

        if(!capitalLettersInPassword) {
            return AuthError.InvalidaccountType
        }

        if(accountType.isBlank()) {
            return AuthError.FieldEmpty
        }
        return null
    }
    fun validateAccountHolderName(accountHolderName: String): String? {
        return if (accountHolderName.isBlank()) {
            "Account holder name cannot be blank"
        } else if (accountHolderName.length < 3) {
            "Account holder name must be at least 3 characters long"
        } else {
            null
        }
    }

    fun validateAccountNumber(accountNumber: String): String? {
        return if (accountNumber.isBlank()) {
            "Account number cannot be blank"
        } else if (!accountNumber.matches(Regex("\\d{9,18}"))) { // Assuming account numbers are between 9 and 18 digits
            "Invalid account number"
        } else {
            null
        }
    }

    fun validateIfscCode(ifscCode: String): String? {
        return if (ifscCode.isBlank()) {
            "IFSC code cannot be blank"
        } else if (!ifscCode.matches(Regex("^[A-Z]{4}0[A-Z0-9]{6}\$"))) { // Assuming a standard IFSC code format
            "Invalid IFSC code"
        } else {
            null
        }
    }
}