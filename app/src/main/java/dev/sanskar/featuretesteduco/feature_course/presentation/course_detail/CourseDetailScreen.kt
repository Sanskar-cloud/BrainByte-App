package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import android.content.SharedPreferences
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.presentation.components.*
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.core.presentation.util.showKeyboard
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.feature_course.presentation.home.FullScreenPaymentDialog
import dev.sanskar.featuretesteduco.feature_course.presentation.home.PaymentDialog
import dev.sanskar.featuretesteduco.ui.theme.Grey400
import dev.sanskar.featuretesteduco.ui.theme.Grey700
import dev.sanskar.featuretesteduco.ui.theme.Grey800
import dev.sanskar.featuretesteduco.ui.theme.TagBG
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CourseDetailScreen(
    scaffoldState: ScaffoldState,
    imageLoader: ImageLoader,
    onNavigate: (String) -> Unit = {},
    sharedPreferences: SharedPreferences,
    onNavigateUp: () -> Unit = {},
    viewModel: CourseDetailViewModel = hiltViewModel(),
    shouldShowKeyboard: Boolean = false
) {

    val state = viewModel.state.collectAsState().value
    val courseOverviewState = viewModel.courseOverviewState.collectAsState().value
    val userState = viewModel.userState.value
    val commentTextFieldState = viewModel.commentTextFieldState.value
    var showDialog by remember { mutableStateOf(false) }
    var paymentLinkUrl by remember { mutableStateOf("") }
    var showWebView by remember { mutableStateOf(false) }
    var paymentSuccessful by remember { mutableStateOf(false) }
    var courseOwned by remember { mutableStateOf(false) }
    val userProfile = viewModel.userProfile.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

//    println(courseOverviewState.courseIntroVideoUrl?: " niiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiddddddffnfdbnbnbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbdfbbgbdfbbfdbsfdbsgbdvbsfnlbsbjdfbvlknsgboldfnbsgj;bkfb;sjbfv")

    val focusRequester = remember {
        FocusRequester()
    }

    val sheetState = rememberBottomSheetState(
        initialValue = BottomSheetValue.Collapsed
    )
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    val expandProgress = bottomSheetScaffoldState.bottomSheetState.expandProgress

    if (expandProgress > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(expandProgress * 0.5f)
                .background(Color.Black)
                .clickable {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
        )
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        if (shouldShowKeyboard) {
            context.showKeyboard()
            focusRequester.requestFocus()
        }
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
                else -> {}
            }
        }

    }
    if (paymentSuccessful) {
        LaunchedEffect(paymentSuccessful) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "Payment successful, you can create the course now"
            )
            state.course?._id?.let { courseId ->
                onNavigate(Screen.LessonScreen.route + "/$courseId")
            }
        }
    }
    if (courseOwned) {
        LaunchedEffect(courseOwned) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = "You have to purchase this course first!!"
            )

        }
    }
    if (showDialog) {
        PaymentDialog(
            onDismiss = { showDialog = false },
            onProceed = {
                userProfile.profile?.email?.let {
                    state.course?.let { it1 ->
                        println("yha pe ni aya h ${it1}")
                        viewModel.initiatePaymentForStudent(
                            sharedPreferences.getString(Constants.KEY_USER_ID, "") ?: "",
                            StudentEmail = it,
                            StudentName = userProfile.profile?.name ?: "",
                            courseId = it1._id
                        ) { url ->
                            if (url.isNotEmpty()) {
                                paymentLinkUrl = url
                                showWebView = true
                            }
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
    println("yha pe ni aya h${state.course?.courseIntroVideoUrl}")
    if(state.isLoadingCourse==true && courseOverviewState.isLoadingOverview==true){
        CircularProgressIndicator()
    }
    else{

    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onNavigateUp() }
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_hearder), contentDescription = null)
            }
            Text(
                text = "Details",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
            IconButton(
                onClick = { viewModel.addCourseToBookmark() }
            ) {
                if (viewModel.isBookmarked.value) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "bookmark icon",
                        tint = Color(0XFF0B121F)
                    )
                } else {
                    Icon(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = "bookmark icon")
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(200.dp)
            ) {
                VideoView(

                    videoUri = state.course?.courseIntroVideoUrl?: " https://coursedaata112.s3.ap-southeast-2.amazonaws.com/Untitled%20video%20-%20Made%20with%20Clipchamp.mp4"
                )
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        println("yha pe ni aya h part 2  ${state.course?.tag}")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {

            state.course?.tag?.let {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier
                        .background(TagBG)
                        .padding(horizontal = 15.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = state.course?.courseTeacher?.profileImageUrl,
                        imageLoader = imageLoader
                    ),
                    contentDescription = "profile Picture of Teacher",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                state.course?.courseTeacher?.username?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Grey700
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            state.course?.courseTitle?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Grey800
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {


                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = "clock",
                            tint = Grey400
                        )
                        Text(
                            text = "1 hour 30 min",
                            color = Grey400,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star_liniar),
                            contentDescription = "rating",
                            tint = Grey400
                        )
                        Text(
                            text = "${state.course?.avgRating}  (${state.course?.noOfStudentRated})      ",
                            color = Grey400,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_video_lesson),
                            contentDescription = "video_lesson",
                            tint = Grey400
                        )
                        Text(
                            text = "${state.course?.noOfLessons}",
                            color = Grey400,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "person",
                            tint = Grey400
                        )
                        Text(
                            text = "${state.course?.noOfStudentEnrolled}  students",
                            color = Grey400,
                            fontSize = 14.sp
                        )
                    }
                }


            }
            Spacer(modifier = Modifier.height(10.dp))

            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = {
                    val userId = sharedPreferences.getString(Constants.KEY_USER_ID, "") ?: ""
                    state.course?._id?.let { courseId ->
                    viewModel.hasStudentPaid(courseId, userId) { isPaid ->
                        if (isPaid) {
                            onNavigate(Screen.LessonScreen.route + "/$courseId")
                        } else {
                            courseOwned=true
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("You need to purchase this course first")
                            }
                            showDialog = true
                        }
                    }}



                },
            ) {
                Text(text = "Watch Lectures")
            }
            TabsLayout(
                courseOverviewState = courseOverviewState,
                comments = state.comments,
                lessons = state.lessons,
                resources = state.resources,
                imageLoader = imageLoader,
                onLikeClick = { commentId ->
                    viewModel.onEvent(CourseDetailEvent.LikeComment(commentId = commentId))
                },

                onLikedByClick = { commentId ->
                    onNavigate(Screen.PersonListScreen.route + "/${commentId}")
                },

//                onLessonClick = { lessonId ->
//                    onNavigate(Screen.LessonDetailScreen.route + "/${lessonId}")
//                },
                commentState = commentTextFieldState,
                onCommentValueChange = {
                    viewModel.onEvent(CourseDetailEvent.EnteredComment(it))
                },
                onSendClick = {
                    viewModel.onEvent(CourseDetailEvent.Comment)
                },
                scaffoldState = bottomSheetScaffoldState
            )

        }
    }}

}


@OptIn(ExperimentalMaterialApi::class)
val BottomSheetState.expandProgress: Float
    get() {
        return when (progress.from) {
            BottomSheetValue.Collapsed -> {
                when (progress.to) {
                    BottomSheetValue.Collapsed -> 0f
                    BottomSheetValue.Expanded -> progress.fraction
                }
            }
            BottomSheetValue.Expanded -> {
                when (progress.to) {
                    BottomSheetValue.Collapsed -> 1f - progress.fraction
                    BottomSheetValue.Expanded -> 1f
                }
            }
        }
    }




private val tabContainerModifier = Modifier
    .fillMaxWidth()
    .wrapContentWidth(Alignment.CenterHorizontally)