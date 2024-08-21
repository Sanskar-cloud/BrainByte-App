package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.SharedPreferences
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.domain.use_case.GetOwnUserIdUseCase
import dev.sanskar.featuretesteduco.core.presentation.components.CourseItem
import dev.sanskar.featuretesteduco.core.presentation.components.CourseOverviewCard
import dev.sanskar.featuretesteduco.core.presentation.components.Courses
import dev.sanskar.featuretesteduco.core.presentation.components.CoursesEnrolled
import dev.sanskar.featuretesteduco.core.presentation.components.CoursesRated
import dev.sanskar.featuretesteduco.core.presentation.components.MostWatchedCourses
import dev.sanskar.featuretesteduco.core.presentation.components.UserHeader
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.feature_auth.presentation.signin.SignInEvent
import dev.sanskar.featuretesteduco.ui.theme.SpaceLarge
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoilApi
@Composable
fun TeacherHomeScreen(
    imageLoader: ImageLoader,
    scaffoldState: ScaffoldState,
    onNavigate: (String) -> Unit = {},
    sharedPreferences: SharedPreferences,
    viewModel: TeacherHomeViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    val profileHeader = viewModel.profileHeaderState.value.profileHeader
    val context = LocalContext.current
    val CoursePagingState = viewModel.CoursePagingState.value
    val CoursePagingRatedState=viewModel.CoursePagingStateRted.value
    val CoursePagingEnrolledState=viewModel.CoursePagingStatePop.value
    val userProfile = viewModel.userProfile.collectAsState().value
    var paymentLinkUrl by remember { mutableStateOf("") }
    var showWebView by remember { mutableStateOf(false) }
    var paymentSuccessful by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
            }
        }
    }

    if (paymentSuccessful) {
        LaunchedEffect(paymentSuccessful) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Payment successful, you can create the course now"
            )
            onNavigate(Screen.CreateCourseScreen.route)
        }
    }

    if (showDialog) {
        PaymentDialog(
            onDismiss = { showDialog = false },
            onProceed = {
                userProfile.profile?.email?.let {
                    viewModel.initiatePayment(
                        sharedPreferences.getString(Constants.KEY_USER_ID, "") ?: "",
                        teacherEmail = it,
                        teacherName = userProfile.profile?.name ?: ""
                    ) { url ->
                        if (url.isNotEmpty()) {
                            paymentLinkUrl = url
                            showWebView = true
                        }
                    }
                }
                showDialog = false
            }
        )
    }

    if (showWebView) {
        FullScreenPaymentDialog(
            url = paymentLinkUrl,
            onDismiss = { showWebView = false },
            onPaymentSuccess = {
                paymentSuccessful = true

            },
            onPaymentFailure = {
                showWebView = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        UserHeader(
            name = profileHeader.name,
            profilePictureUrl = profileHeader.profilePictureUrl
        )
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    Courses(onNavigate)
                    Spacer(modifier = Modifier.height(30.dp))
                    LazyRow {
                        items(CoursePagingState.items.size) { i ->
                            val course = CoursePagingState.items[i]
                            if (i >= CoursePagingState.items.size - 1 && !CoursePagingState.endReached && !CoursePagingState.isLoading) {
                                viewModel.loadCoursesForTchr()
                            }
                            CourseOverviewCard(
                                course = course,
                                imageLoader = imageLoader,
                                onClick = {
                                    onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}")
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            if (i < CoursePagingState.items.size - 1) {
                                Spacer(modifier = Modifier.width(SpaceLarge))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(90.dp))
                        }
                    }
                    CoursesRated(onNavigate)
                    Spacer(modifier = Modifier.height(30.dp))
                    LazyRow {
                        items(CoursePagingRatedState.items.size) { i ->
                            val course = CoursePagingRatedState.items[i]
                            if (i >= CoursePagingRatedState.items.size - 1 && !CoursePagingRatedState.endReached && !CoursePagingRatedState.isLoading) {
                                viewModel.loadCoursesForTchrRated()
                            }
                            CourseOverviewCard(
                                course = course,
                                imageLoader = imageLoader,
                                onClick = {
                                    onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}")
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            if (i < CoursePagingRatedState.items.size - 1) {
                                Spacer(modifier = Modifier.width(SpaceLarge))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.width(90.dp))
                        }
                    }
                    CoursesEnrolled(onNavigate)
                    Spacer(modifier = Modifier.height(30.dp))
                    LazyRow {
                        items(CoursePagingEnrolledState.items.size) { i ->
                            val course = CoursePagingEnrolledState.items[i]
                            if (i >= CoursePagingEnrolledState.items.size - 1 && !CoursePagingEnrolledState.endReached && !CoursePagingEnrolledState.isLoading) {
                                viewModel.loadCoursesForTchrEnrolled()
                            }
                            CourseOverviewCard(
                                course = course,
                                imageLoader = imageLoader,
                                onClick = {
                                    onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}")
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            if (i < CoursePagingEnrolledState.items.size - 1) {
                                Spacer(modifier = Modifier.width(SpaceLarge))
                            }
                        }

                    }
                }
            }
            item{
                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 40.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.login),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }

    }
}

@Composable
fun FullScreenPaymentDialog(
    url: String,
    onDismiss: () -> Unit,
    onPaymentSuccess: () -> Unit,
    onPaymentFailure: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            AndroidView(factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val requestUrl = request?.url.toString()
                            when {
                                requestUrl.contains("razorpay_payment_link_=paid")-> {
                                    view?.loadData("Payment successful, you can create the course now. Loading Course page...", "text/html", "UTF-8")
                                    onPaymentSuccess()
                                }
                                requestUrl.contains("razorpay_payment_link_status=failed") -> onPaymentFailure()
                                else -> view?.loadUrl(requestUrl) // Continue loading the URL if not a special case
                            }
                            return true
                        }
                    }
                    loadUrl(url)
                }
            })
        },
        buttons = {}
    )
}

@Composable
fun PaymentDialog(onDismiss: () -> Unit, onProceed: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Payment Required") },
        text = { Text(text = "Please pay Rs. 2500 to create a new course!") },
        confirmButton = {
            Button(
                onClick = {
                    onProceed()
                }
            ) {
                Text("Proceed to Pay")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancel")
            }
        }
    )
}
