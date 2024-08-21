package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.models.CourseResource
import dev.sanskar.featuretesteduco.core.presentation.components.ResourcesForDownload
import dev.sanskar.featuretesteduco.core.presentation.components.TabsLayout
import dev.sanskar.featuretesteduco.core.presentation.components.VideoView
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.core.presentation.util.showKeyboard
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.Grey400
import dev.sanskar.featuretesteduco.ui.theme.Grey800
import dev.sanskar.featuretesteduco.ui.theme.TagBG
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LessonDetailScreen(

    _id: String?,
    scaffoldState: ScaffoldState,
    imageLoader: ImageLoader,
    onNavigate: (String) -> Unit = {},
    onNavigateUp: () -> Unit = {},
    viewModel: LessonDetailViewModel = hiltViewModel(),
    shouldShowKeyboard: Boolean = false
) {


//    val courseOverviewState = viewModel.courseOverviewState.collectAsState().value
    val lessonDetailState=viewModel.lessonsState.collectAsState().value
    val resourcestate=viewModel.resourcesState.collectAsState().value

//    val userState = viewModel.userState.value
//    val commentTextFieldState = viewModel.commentTextFieldState.value
//    println(courseOverviewState.courseIntroVideoUrl?: " niiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiddddddffnfdbnbnbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbdfbbgbdfbbfdbsfdbsgbdvbsfnlbsbjdfbvlknsgboldfnbsgj;bkfb;sjbfv")
    LaunchedEffect(_id) {
        _id?.let { viewModel.loadLessonDetails(it) }
    }
    LaunchedEffect(_id) {
        _id?.let { viewModel.loadResources(it) }
    }
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

    if(lessonDetailState.isLoadingLesson==true ){
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
                    text = "Lesson Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

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

                        videoUri = lessonDetailState.lesson?.lessonVideoUrl?: " https://coursedaata112.s3.ap-southeast-2.amazonaws.com/Untitled%20video%20-%20Made%20with%20Clipchamp.mp4"
                    )
                }

            }
            Spacer(modifier = Modifier.height(40.dp))
            if (resourcestate.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(resourcestate.resources) { resource ->
                        ResourcesForDownload(courseResource = resource)
                    }
//                    ResourceItem(
//                        resources = lesson,
//                        imageLoader = imageLoader,
//                        onNavigate = onNavigate
//                    )


                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }}
                }
            }


            }


        }


@Composable
fun ResourceItem(resources:CourseResource, imageLoader: ImageLoader, onNavigate: (String) -> Unit) {



}





