package dev.sanskar.featuretesteduco.feature_auth.domain.use_case

import dev.sanskar.featuretesteduco.core.domain.util.ValidationUtil
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.AccountDetails

import dev.sanskar.featuretesteduco.feature_auth.domain.models.SignUpResult
import dev.sanskar.featuretesteduco.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(
        email: String,
        username: String,
        password: String,
        accountType: String,
        accountDetails: AccountDetails?
    ): SignUpResult {
        val emailError = ValidationUtil.validateEmail(email)
        val usernameError = ValidationUtil.validateUsername(username)
        val passwordError = ValidationUtil.validatePassword(password)
        val accountTypeError = ValidationUtil.validateAccountType(accountType)
        val accountHolderNameError = accountDetails?.let {
            ValidationUtil.validateAccountHolderName(
                it.accountHolderName)
        }
        val accountNumberError = accountDetails?.let { ValidationUtil.validateAccountNumber(it.accountNumber) }
        val ifscCodeError = accountDetails?.let { ValidationUtil.validateIfscCode(it.ifscCode) }

        if (emailError != null || usernameError != null || passwordError != null || accountTypeError != null ||
            accountHolderNameError != null || accountNumberError != null || ifscCodeError != null) {
            return SignUpResult(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError,
                accountTypeError = accountTypeError,
                accountHolderNameError = accountHolderNameError,
                accountNumberError = accountNumberError,
                ifscCodeError = ifscCodeError
            )
        }

        val result = repository.signUp(
            email.trim(),
            username.trim(),
            password.trim(),
            accountType.trim(),
            accountDetails?.copy(
                accountHolderName = accountDetails.accountHolderName.trim(),
                accountNumber = accountDetails.accountNumber.trim(),
                ifscCode = accountDetails.ifscCode.trim()
            )
        )

        return SignUpResult(
            result = result
        )
    }
}
