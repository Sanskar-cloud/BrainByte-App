package dev.sanskar.featuretesteduco.feature_course.presentation.category_list

import android.app.TimePickerDialog
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.text.Layout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.presentation.components.StandardTextField
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.feature_course.presentation.home.CreateCourseEvent
import dev.sanskar.featuretesteduco.feature_course.presentation.home.TeacherHomeViewModel
import dev.sanskar.featuretesteduco.feature_settings.presentation.SettingsEvent
import dev.sanskar.featuretesteduco.feature_settings.presentation.SettingsViewModel
import dev.sanskar.featuretesteduco.ui.theme.Error500
import dev.sanskar.featuretesteduco.ui.theme.Primary600
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CreateCourseScreenPreview() {
    // Mock the necessary dependencies for the preview
    val scaffoldState = rememberScaffoldState()
    val imageLoader = ImageLoader(LocalContext.current)



    CreateCourseScreen(
        onNavigateUp = {},
        onNavigate = {},
        imageLoader = imageLoader,
        scaffoldState = scaffoldState

    )
}



@Composable
fun CreateCourseScreen(
    onNavigateUp: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    imageLoader: ImageLoader,
    scaffoldState: ScaffoldState,
    viewModel: TeacherHomeViewModel = hiltViewModel()
) {

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        viewModel.selectedImageUri.value = uri
    }
    val launcher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
        viewModel.selectedVideoUri.value = uri
    }

    val focusRequester = remember { FocusRequester() }
    val videoUri by viewModel.selectedVideoUri.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar(onNavigateUp = onNavigateUp)

        Spacer(modifier = Modifier.height(10.dp))

        ProfileSection(imageLoader, viewModel, launcher, launcher2, videoUri)

        Spacer(modifier = Modifier.height(10.dp))

        CourseDetailsSection(viewModel, focusRequester)

        Spacer(modifier = Modifier.height(20.dp))

        ActionButtons(viewModel, context)
    }
}

@Composable
fun TopBar(onNavigateUp: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onNavigateUp() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_hearder),
                contentDescription = "Back Icon"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Welcome!! Create a New Course",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ProfileSection(
    imageLoader: ImageLoader,
    viewModel: TeacherHomeViewModel,
    launcher: ActivityResultLauncher<String>,
    launcher2: ActivityResultLauncher<String>,
    videoUri: Uri?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom

    ) {

        Column {
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(
                    model = viewModel.selectedImageUri.value,
                    imageLoader = imageLoader
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
//                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "Change profile picture",
                color = Primary600,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { launcher.launch("image/*") }
                    .padding(end =30.dp)
            )

        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {

            Spacer(modifier = Modifier.height(1.dp))
            videoUri?.let {
                VideoThumbnail(uri = it)
            }
            Text(
                text = "Change profile video",
                color = Primary600,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { launcher2.launch("video/*") }
                    .padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun CourseDetailsSection(viewModel: TeacherHomeViewModel, focusRequester: FocusRequester) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(
            value = viewModel.titleState.value.text,
            onValueChange = { viewModel.onEvent(CreateCourseEvent.EnteredCourseTitle(it)) },
            label = "Course Title",
            maxLength = 50
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            value = viewModel.descriptionState.value.text,
            onValueChange = { viewModel.onEvent(CreateCourseEvent.EnteredDescription(it)) },
            label = "Description",
            maxLength = 1000,
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            value = viewModel.tagsState.value.text,
            onValueChange = { viewModel.onEvent(CreateCourseEvent.EnteredTag(it)) },
            label = "Tag",
            maxLength = 10
        )
        CustomTextField(
            value = viewModel.priceState.value.text,
            onValueChange = { viewModel.onEvent(CreateCourseEvent.EnteredPrice(it)) },
            label = "Enter Price of The Course",
            maxLength = 10
        )
        Spacer(modifier = Modifier.height(10.dp))
        DurationSelector(
            duration = viewModel.durationState.value.text,
            onValueChange = { viewModel.onEvent(CreateCourseEvent.EnteredDuration(it)) }
        )
    }
}

@Composable
fun ActionButtons(viewModel: TeacherHomeViewModel, context: Context) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { viewModel.onEvent(CreateCourseEvent.Cancel) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Cancel",
                color = Error500,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Button(
            onClick = { viewModel.onEvent(CreateCourseEvent.SaveCourse(context)) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Primary600),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = "Save",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    maxLength: Int,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) onValueChange(it)
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Primary600,
            unfocusedBorderColor = Color.Gray
        )
    )
}

//@Composable
//fun DurationSelector(duration: String, onValueChange: (String) -> Unit) {
//    var sliderPosition by remember { mutableStateOf(0f) }
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(text = "Video Duration: $duration mins", fontSize = 14.sp)
//        Slider(
//            value = sliderPosition,
//            onValueChange = {
//                sliderPosition = it
//                onValueChange(it.toInt().toString())
//            },
//            valueRange = 0f..60f,
//            steps = 6,
//            colors = SliderDefaults.colors(
//                thumbColor = Primary600,
//                activeTrackColor = Primary600
//            )
//        )
//    }
//}

//@Composable
//fun DurationSelector(duration: String, onValueChange: (String) -> Unit) {
//    val context = LocalContext.current
//    val hour = remember { mutableStateOf(0) }
//    val minute = remember { mutableStateOf(0) }
//    val timePickerDialog = TimePickerDialog(
//        context,
//        { _, selectedHour, selectedMinute ->
//            hour.value = selectedHour
//            minute.value = selectedMinute
//            onValueChange("$selectedHour hrs $selectedMinute mins")
//        }, hour.value, minute.value, true
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(text = "Video Duration: $duration", fontSize = 14.sp)
//        Button(
//            onClick = {
//                timePickerDialog.show()
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Primary600),
//            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
//        ) {
//            Text(
//                text = "Select Duration",
//                color = Color.White,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Medium
//            )
//        }
//    }
//}
//@Composable
//fun DurationSelector(duration: String, onValueChange: (String) -> Unit) {
//    val hours = (0..23).toList()
//    val minutes = (0..59).toList()
//
//    var selectedHour by remember { mutableStateOf(0) }
//    var selectedMinute by remember { mutableStateOf(0) }
//
//    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Video Duration: $duration", fontSize = 14.sp)
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Row(
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(150.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                items(hours) { hour ->
//                    Text(
//                        text = hour.toString().padStart(2, '0'),
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .clickable {
//                                selectedHour = hour
//                                onValueChange("${selectedHour} hrs ${selectedMinute} mins")
//                            },
//                        fontSize = 24.sp
//                    )
//                }
//            }
//
//            Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(150.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                items(minutes) { minute ->
//                    Text(
//                        text = minute.toString().padStart(2, '0'),
//                        modifier = Modifier
//                            .padding(8.dp)
//                            .clickable {
//                                selectedMinute = minute
//                                onValueChange("${selectedHour} hrs ${selectedMinute} mins")
//                            },
//                        fontSize = 24.sp
//                    )
//                }
//            }
//        }
//    }}

//@Composable
//fun DurationSelector(duration: String, onValueChange: (String) -> Unit) {
//    val hours = (0..23).toList()
//    val minutes = (0..59).toList()
//
//    var selectedHour by remember { mutableStateOf(0) }
//    var selectedMinute by remember { mutableStateOf(0) }
//
//    // Function to update the duration string
//    val updateDuration = {
//        onValueChange("${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}")
//    }
//
//    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = "Video Duration: $duration", fontSize = 14.sp)
//
//       Spacer(modifier = Modifier.height(10.dp))
//
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .height(150.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            items(hours) { hour ->
//                Text(
//                    text = hour.toString().padStart(2, '0'),
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .clickable {
//                            selectedHour = hour
//                            updateDuration()
//                        },
//                    fontSize = 24.sp
//                )
//            }
//        }
//
//        Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
//
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .height(150.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            items(minutes) { minute ->
//                Text(
//                    text = minute.toString().padStart(2, '0'),
//                    modifier = Modifier
//                        .padding(8.dp)
//                        .clickable {
//                            selectedMinute = minute
//                            updateDuration()
//                        },
//                    fontSize = 24.sp
//                )
//            }
//        }
//    }}
//
//    // Automatically update duration as the values are selected
//    LaunchedEffect(selectedHour, selectedMinute) {
//        updateDuration()
//    }
//}


@Composable
fun DurationSelector(duration: String, onValueChange: (String) -> Unit) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()

    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()

    val selectedHour = remember { derivedStateOf { (hourListState.firstVisibleItemIndex + 1) % hours.size } }
    val selectedMinute = remember { derivedStateOf { (minuteListState.firstVisibleItemIndex + 1) % minutes.size } }

    // Function to update the duration string
    val updateDuration = {
        onValueChange("${selectedHour.value.toString().padStart(2, '0')}:${selectedMinute.value.toString().padStart(2, '0')}")
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Video Duration: $duration", fontSize = 14.sp)

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyColumn(
                state = hourListState,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(hours) { hour ->
                    Text(
                        text = hour.toString().padStart(2, '0'),
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 24.sp
                    )
                }
            }

            Text(text = ":", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            LazyColumn(
                state = minuteListState,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(minutes) { minute ->
                    Text(
                        text = minute.toString().padStart(2, '0'),
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 24.sp
                    )
                }
            }
        }
    }

    // Automatically update duration as the values are scrolled
    LaunchedEffect(selectedHour.value, selectedMinute.value) {
        updateDuration()
    }
}




@Composable
fun VideoThumbnail(uri: Uri) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(uri) {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        bitmap = retriever.frameAtTime
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    } ?: Box(
        modifier = Modifier
            .size(120.dp)
            .background(Color.Gray)
    )
}
