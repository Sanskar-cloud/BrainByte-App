package dev.sanskar.featuretesteduco.core.presentation.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.sanskar.featuretesteduco.core.domain.models.CourseOverview
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.Grey500
import dev.sanskar.featuretesteduco.ui.theme.Grey800
import dev.sanskar.featuretesteduco.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*


import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.feature_auth.presentation.signin.SignInEvent
import dev.sanskar.featuretesteduco.feature_course.presentation.home.CreateCourseEvent
import dev.sanskar.featuretesteduco.feature_course.presentation.home.CreateLessonEvent
import dev.sanskar.featuretesteduco.feature_course.presentation.home.DeleteCourseEvent
import dev.sanskar.featuretesteduco.feature_course.presentation.home.LessonViewmodel
import dev.sanskar.featuretesteduco.feature_course.presentation.home.TeacherHomeViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CourseItemForTeacher(
    course: CourseOverview,
    onNavigate: (String) -> Unit = {},
    scaffoldState: ScaffoldState,

    imageLoader: ImageLoader,
    viewModel: TeacherHomeViewModel= hiltViewModel(),
    vieModel2: LessonViewmodel = hiltViewModel()

) {
    val state2=vieModel2.createCourseResource.value
    val state = viewModel.deleteCourse.value
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showUploadDialog by remember { mutableStateOf(false) }
    var showUploadLoader by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        vieModel2.selectedResourceUri.value=uris}

    LaunchedEffect(key1 = true){
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
    LaunchedEffect(key1 = true) {
        vieModel2.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
                is UiEvent.Navigate -> {
                    onNavigate(event.route) // Trigger navigation to the specified route
                }

                else -> {}
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Course") },
            text = { Text("Are you sure you want to delete this course?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        viewModel.onEvent(CreateCourseEvent.DeleteCourse(course.courseId))


                    }
                ) {
                    Text("Yes")
                }
                if (state.isLoading) {
                    CircularProgressIndicator()
                }

            },

            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("No")
                }
            }
        )
    }
    if(showUploadDialog){
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Upload Resources") },
            text ={Column {
                Text("Upload resources for this course")

            }},
            confirmButton = {
                TextButton(
                    modifier = Modifier.background(Color.White),
                    onClick = {
                        // Handle file upload here
                        vieModel2.onEvent(CreateLessonEvent.UploadResource(context,course.courseId))
                        showUploadLoader = true
                        if(vieModel2.createCourseResource.value?.isLoading==false){
                            showUploadDialog = false

                        }



                    }
                ) {
                    if(showUploadLoader==true){

                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressIndicator()


                    }
                    else{
                        Text(
                            text = "Upload Files",
                            color = Color.Black
                        )

                    }

                }



            },
            dismissButton = {
                Button(
                    modifier = Modifier.background(Color.White),
                    onClick = {
                        // Launch the file picker with multiple MIME types
                        filePickerLauncher.launch(arrayOf("image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    }
                ) {
                    if(showUploadLoader==true){

                    }
                    else{
                        Text(
                            text = "Choose Files",
                            color = Color.Black
                        )

                    }

                }
            }
        )

//
    }
//    if (showUploadLoader) {
//        Spacer(modifier = Modifier.height(16.dp))
//        CircularProgressIndicator()
//    }

    // Handle hiding loader when upload completes



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickable { onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}") }
        ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                painter = rememberAsyncImagePainter(
                    model = course.courseThumbnailUrl,
                    imageLoader = imageLoader
                ),
                contentDescription = "course thumbnail"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = course.courseName,
                    fontSize = 18.sp,
                    color = Grey800
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(id = R.drawable.ic_user_1), contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${course.noOfStudentEnrolled} student",
                        fontSize = 12.sp,
                        color = Grey500
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, tint = Color(0XffFFA927))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${course.rating}",
                        fontSize = 12.sp,
                        color = Grey500
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.Top)
                .padding(end = 8.dp)
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    showDialog = true
                }) {
                    Text("Delete Course")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    // Navigate to Create Lesson screen
                    onNavigate(Screen.CreateLessonScreen.route + "/${course.courseId}")
                }) {
                    Text("Create Lesson")
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    showUploadDialog = true

                }) {
                    Text("Upload Resources")
//                    if (state2?.isLoading == true) {
//                        Spacer(modifier = Modifier.height(16.dp))
//                        CircularProgressIndicator()
//                    }
                }
            }
        }
    }
}


