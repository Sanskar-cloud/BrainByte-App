package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.presentation.util.UiEvent
import dev.sanskar.featuretesteduco.core.presentation.util.asString
import dev.sanskar.featuretesteduco.feature_course.presentation.category_list.DurationSelector
import dev.sanskar.featuretesteduco.feature_course.presentation.category_list.VideoThumbnail
import dev.sanskar.featuretesteduco.ui.theme.Error500
import dev.sanskar.featuretesteduco.ui.theme.Primary600
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateLessonScreen(
    courseId: String,
    onNavigateUp: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    imageLoader: ImageLoader,
    scaffoldState: ScaffoldState,
    viewModel: LessonViewmodel = hiltViewModel()
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
    val isLoading=viewModel.createLesson.value.isLoading

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

        ActionButtons(viewModel, context, courseId = courseId)
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
            text = "Welcome!! Create a New Lesson",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ProfileSection(
    imageLoader: ImageLoader,
    viewModel: LessonViewmodel,
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
                text = "Choose Lesson Thumbnail Here!",
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
                text = "Choose Lesson Video Here!",
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
fun CourseDetailsSection(viewModel: LessonViewmodel, focusRequester: FocusRequester) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CustomTextField(
            value = viewModel.lessonnoState.value.text,
            onValueChange = { viewModel.onEvent(CreateLessonEvent.EnteredLessonNo(it)) },
            label = "Lesson No.",
            maxLength = 3
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            value = viewModel.nameState.value.text,
            onValueChange = { viewModel.onEvent(CreateLessonEvent.EnteredLessonName(it)) },
            label = "Name",
            maxLength = 1000,
            modifier = Modifier.height(150.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomTextField(
            value = viewModel.descState.value.text,
            onValueChange = { viewModel.onEvent(CreateLessonEvent.EnteredDescription(it)) },
            label = "Description",
            maxLength = 10
        )
        Spacer(modifier = Modifier.height(10.dp))

    }
}

@Composable
fun ActionButtons(viewModel: LessonViewmodel, context: Context, courseId: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { viewModel.onEvent(CreateLessonEvent.Cancel) },
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
            onClick = { viewModel.onEvent(CreateLessonEvent.SaveLesson(context, courseId = courseId)) },
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
    if(viewModel.createLesson.value.isLoading==true){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }}
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
