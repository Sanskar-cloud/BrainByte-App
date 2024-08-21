package dev.sanskar.featuretesteduco.feature_auth.presentation.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.states.PasswordTextFieldState
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState
import dev.sanskar.featuretesteduco.core.domain.states.StandardTextFieldState2
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.util.Resource
import dev.sanskar.featuretesteduco.core.util.UiText
import dev.sanskar.featuretesteduco.feature_auth.data.remote.request.AccountDetails
import dev.sanskar.featuretesteduco.feature_auth.domain.use_case.SignUpUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signupUseCase: SignUpUseCase,



    ) : ViewModel() {

//    private val _otpState = mutableStateOf(StandardTextFieldState())
//    val otpState: State<StandardTextFieldState> = _otpState
     private val _accountTypeState = mutableStateOf(StandardTextFieldState())
    val accountTypeState: State<StandardTextFieldState> = _accountTypeState

    private val _emailState = mutableStateOf(StandardTextFieldState())
    val emailState: State<StandardTextFieldState> = _emailState

    private val _usernameState = mutableStateOf(StandardTextFieldState())
    val usernameState: State<StandardTextFieldState> = _usernameState

    private val _passwordState = mutableStateOf(PasswordTextFieldState())
    val passwordState: State<PasswordTextFieldState> = _passwordState

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private val _onRegister = MutableSharedFlow<Unit>(replay = 1)
    val onRegister = _onRegister.asSharedFlow()
    private val _accountNumberState = mutableStateOf(StandardTextFieldState2())
    val accountNumberState: State<StandardTextFieldState2> = _accountNumberState

    private val _ifscCodeState = mutableStateOf(StandardTextFieldState2())
    val ifscCodeState: State<StandardTextFieldState2> = _ifscCodeState

    private val _accountHolderNameState = mutableStateOf(StandardTextFieldState2())
    val accountHolderNameState: State<StandardTextFieldState2> = _accountHolderNameState

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.EnteredUsername -> {
                _usernameState.value = _usernameState.value.copy(
                    text = event.value
                )
            }
            is SignUpEvent.EnteredUserType -> {
                _accountTypeState.value = _accountTypeState.value.copy(
                    text = event.value
                )
            }

            is SignUpEvent.EnteredEmail -> {
                _emailState.value = _emailState.value.copy(
                    text = event.value
                )
            }

            is SignUpEvent.EnteredPassword -> {
                _passwordState.value = _passwordState.value.copy(
                    text = event.value
                )
            }

            is SignUpEvent.TogglePasswordVisibility -> {
                _passwordState.value = _passwordState.value.copy(
                    isPasswordVisible = !passwordState.value.isPasswordVisible
                )
            }
            is SignUpEvent.EnteredAccountNumber -> {
                _accountNumberState.value = accountNumberState.value.copy(
                    text = event.value,

                )
            }
            is SignUpEvent.EnteredIfscCode -> {
                _ifscCodeState.value = ifscCodeState.value.copy(
                    text = event.value
                )
            }
            is SignUpEvent.EnteredAccountHolderName -> {
                _accountHolderNameState.value = accountHolderNameState.value.copy(
                    text = event.value
                )
            }
//            is SignUpEvent.EnteredOtp -> {
//                _otpState.value = _otpState.value.copy(
//                    text = event.value
//                )
//            }

            is SignUpEvent.SignUp -> {
                signUp()
            }


            else -> Unit
        }

    }


    private fun signUp() {
        viewModelScope.launch {
            _usernameState.value = usernameState.value.copy(error = null)
            _emailState.value = emailState.value.copy(error = null)
            _passwordState.value = passwordState.value.copy(error = null)
            _accountTypeState.value = accountTypeState.value.copy(error = null)
//            _otpState.value = otpState.value.copy(error = null)
            _registerState.value = RegisterState(isLoading = true)
            val accountDetails: AccountDetails? = if (accountTypeState.value.text == "Teacher") {
                AccountDetails(
                    accountHolderName = accountHolderNameState.value.text,
                    accountNumber = accountNumberState.value.text,
                    ifscCode = ifscCodeState.value.text
                )
            } else {
                null
            }
            val signUpResult = signupUseCase(
                email = emailState.value.text,
                username = usernameState.value.text,
                password = passwordState.value.text,
                accountType = accountTypeState.value.text,
                accountDetails

            )
            if (signUpResult.emailError != null) {
                _emailState.value = emailState.value.copy(
                    error = signUpResult.emailError
                )
            }
            if (signUpResult.usernameError != null) {
                _usernameState.value = _usernameState.value.copy(
                    error = signUpResult.usernameError
                )
            }
            if (signUpResult.passwordError != null) {
                _passwordState.value = _passwordState.value.copy(
                    error = signUpResult.passwordError
                )
            }
            if (signUpResult.accountTypeError != null) {
                _accountTypeState.value = _accountTypeState.value.copy(
                    error = signUpResult.accountTypeError
                )
            }
            if (accountTypeState.value.text == "Teacher") {
                if (signUpResult.accountHolderNameError != null) {
                    _accountHolderNameState.value = StandardTextFieldState2(
                        text = _accountHolderNameState.value.text,
                        error = signUpResult.accountHolderNameError
                    )
                }
                if (signUpResult.accountNumberError != null) {
                    _accountNumberState.value = StandardTextFieldState2(
                        text = _accountNumberState.value.text,
                        error = signUpResult.accountNumberError
                    )
                }
                if (signUpResult.ifscCodeError != null) {
                    _ifscCodeState.value = StandardTextFieldState2(
                        text = _ifscCodeState.value.text,
                        error = signUpResult.ifscCodeError
                    )
                }
            }
            when (signUpResult.result) {
                is Resource.Success -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(UiText.StringResource(R.string.success_registration))
                    )
                    _onRegister.emit(Unit)
                    _registerState.value = RegisterState(isLoading = false)
                    _usernameState.value = StandardTextFieldState()
                    _emailState.value = StandardTextFieldState()
                    _passwordState.value = PasswordTextFieldState()
                    _accountTypeState.value = StandardTextFieldState()
                    _accountHolderNameState.value = StandardTextFieldState2()
                    _accountNumberState.value = StandardTextFieldState2()
                    _ifscCodeState.value = StandardTextFieldState2()
//                    _otpState.value = StandardTextFieldState()
                }

                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(signUpResult.result.uiText ?: UiText.unknownError())
                    )
                    _registerState.value = RegisterState(isLoading = false)
                }

                null -> {
                    _registerState.value = RegisterState(isLoading = false)
                }
            }
        }
    }
}