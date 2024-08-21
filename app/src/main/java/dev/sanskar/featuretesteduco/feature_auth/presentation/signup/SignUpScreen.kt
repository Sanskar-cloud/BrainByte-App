package dev.sanskar.featuretesteduco.feature_auth.presentation.signup

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.feature_auth.presentation.util.AuthError
import dev.sanskar.featuretesteduco.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
@Composable
fun SignUpScreen(
    scaffoldState: ScaffoldState,
    onPopBackStack: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = true) {
        viewModel.onRegister.collect {
            onPopBackStack()
        }
    }

    LaunchedEffect(key1 = keyboardController) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    keyboardController?.hide()
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.uiText.asString(context),
                        duration = SnackbarDuration.Long
                    )
                }

                is UiEvent.Navigate -> TODO()
                UiEvent.NavigateUp -> TODO()
                UiEvent.OnResetPassword -> TODO()
                UiEvent.OnSignIn -> TODO()
                UiEvent.OnSignOut -> TODO()
            }
        }
    }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpaceLarge)
            .padding(bottom = SpaceMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = SpaceLarge)
        ) {
            Row {


                Text(
                    text = stringResource(id = R.string.signup),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = SpaceMedium)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            expanded = !expanded
                        }
                        .background(MaterialTheme.colors.surface)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            shape = Shapes.medium
                        )
                        .padding(horizontal = SpaceMedium, vertical = SpaceSmall)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = viewModel.accountTypeState.value.text,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_vertical_menu),
                            contentDescription = stringResource(id = R.string.dropdown_icon),
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier.padding(end = SpaceMedium)
                        )
                    }
                }

                // Dropdown menu items
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("Teacher", "Student").forEach { type ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.onEvent(SignUpEvent.EnteredUserType(type))
                                expanded = false
                            }
                        ) {
                            Text(text = type)
                        }
                    }
                }}
            Spacer(modifier = Modifier.height(SpaceLarge))
            SignUpTextField(
                value = viewModel.emailState.value.text,
                onValueChange = {
                    viewModel.onEvent(SignUpEvent.EnteredEmail(it)) },
                error = when (val error = viewModel.emailState.value.error) {
                    is AuthError.FieldEmpty, is AuthError.InvalidEmail -> error.toString()
                    else -> null
                },
                hint = stringResource(id = R.string.email)
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            SignUpTextField(
                value = viewModel.usernameState.value.text,
                onValueChange = { viewModel.onEvent(SignUpEvent.EnteredUsername(it)) },
                error = when (val error = viewModel.usernameState.value.error) {
                    is AuthError.FieldEmpty, is AuthError.InputTooShort -> error.toString()
                    else -> null
                },
                hint = stringResource(id = R.string.username)
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            SignUpTextField(
                value = viewModel.passwordState.value.text,
                onValueChange = { viewModel.onEvent(SignUpEvent.EnteredPassword(it)) },
                error = when (val error = viewModel.passwordState.value.error) {
                    is AuthError.FieldEmpty, is AuthError.InputTooShort, is AuthError.InvalidPassword -> error.toString()
                    else -> null
                },
                hint = stringResource(id = R.string.password_hint),
                keyboardType = KeyboardType.Password,
                isPasswordVisible = viewModel.passwordState.value.isPasswordVisible,
                onPasswordToggleClick = { viewModel.onEvent(SignUpEvent.TogglePasswordVisibility) }
            )
            Spacer(modifier = Modifier.height(SpaceMedium))


            Spacer(modifier = Modifier.height(SpaceMedium))

            // Account Details (visible only for Teacher)
            if (viewModel.accountTypeState.value.text == "Teacher") {
                SignUpTextField(
                    value = viewModel.accountHolderNameState.value.text,
                    onValueChange = {
                        viewModel.onEvent(SignUpEvent.EnteredAccountHolderName(it))
                    },
                    error = viewModel.accountHolderNameState.value.error,
                    hint = stringResource(id = R.string.account_holder_name)
                )
                Spacer(modifier = Modifier.height(SpaceMedium))

                SignUpTextField(
                    value = viewModel.accountNumberState.value.text,
                    onValueChange = {
                        viewModel.onEvent(SignUpEvent.EnteredAccountNumber(it))
                    },
                    error = viewModel.accountNumberState.value.error,
                    hint = stringResource(id = R.string.account_number)
                )
                Spacer(modifier = Modifier.height(SpaceMedium))

                SignUpTextField(
                    value = viewModel.ifscCodeState.value.text,
                    onValueChange = {
                        viewModel.onEvent(SignUpEvent.EnteredIfscCode(it))
                    },
                    error = viewModel.ifscCodeState.value.error,
                    hint = stringResource(id = R.string.ifsc_code)
                )
                Spacer(modifier = Modifier.height(SpaceMedium))
            }

            SignUpButton(
                text = stringResource(id = R.string.signup),
                onClick = { viewModel.onEvent(SignUpEvent.SignUp) },
                isLoading = viewModel.registerState.value.isLoading
            )
        }
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.already_have_an_account))
                append(" ")
                val signUpText = stringResource(id = R.string.sign_in)
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary
                    )
                ) {
                    append(signUpText)
                }
            },
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable {
                    onPopBackStack()
                }
                .padding(bottom = SpaceLarge)
        )
    }
}
@Composable
private fun SignUpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPasswordVisible: Boolean = false,
    onPasswordToggleClick: () -> Unit = {}
) {
    var interactionSource by remember { mutableStateOf(MutableInteractionSource()) }

    val borderColor by animateFloatAsState(
        targetValue = if (error != null) 1f else 0f
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = error != null,
        label = { Text(text = hint) },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next
        ),
//        keyboardActions = KeyboardActions(
//            onNext = { interactionSource.tryEmit(interactionSource.collectAsMap() + ("submit" to true)) }
//        ),
        trailingIcon = {
            if (isPasswordVisible) {
                IconButton(onClick = onPasswordToggleClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = stringResource(R.string.password_visibility_toggle),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            } else {
                IconButton(onClick = onPasswordToggleClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = stringResource(R.string.password_visibility_toggle),
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .clip(Shapes.medium)
            .border(
                width = 1.dp,
                color = if (error != null) MaterialTheme.colors.error else
                    MaterialTheme.colors.onSurface.copy(alpha = borderColor),
                shape = Shapes.medium
            )
            .padding(horizontal = SpaceMedium, vertical = SpaceSmall)
    )
    error?.let { error ->
        Text(
            text = error,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(start = SpaceMedium, top = SpaceSmall)
        )
    }
}
@Composable
private fun SignUpButton(
    text: String,
    onClick: () -> Unit,
    isLoading: Boolean
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colors.onPrimary
            )
        } else {
            Text(
                text = text,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}
@Composable
private fun GenerateOTP(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,

        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = text,
            color = MaterialTheme.colors.onPrimary
        )
    }
}