package dev.sanskar.featuretesteduco.feature_course.presentation.course_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.domain.models.Lesson
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.Grey500
import dev.sanskar.featuretesteduco.ui.theme.Grey600
import dev.sanskar.featuretesteduco.ui.theme.Grey800
import dev.sanskar.featuretesteduco.ui.theme.Grey900
import dev.sanskar.featuretesteduco.ui.theme.title2new

@Composable
fun LessonScreen(


    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit,
    imageLoader: ImageLoader,
    viewModel: CourseLessonsViewModel = hiltViewModel()
) {
    val lessonState = viewModel.lessonState.collectAsState().value


//    LaunchedEffect(courseId) {
//        viewModel.loadLessons(courseId)
//    }



    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onNavigateUp() },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_user), contentDescription = null)
            }
            Text(text = "Lessons", color = Grey900, style = title2new)
        }

        if (lessonState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(lessonState.lessons.size) { i ->
                    val lesson = lessonState.lessons[i]
                    LessonItem(
                        lesson = lesson,
                        imageLoader = imageLoader,
                        onNavigate = onNavigate
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }
    }
}

@Composable
fun LessonItem(
    lesson: Lesson,
    imageLoader: ImageLoader,
    onNavigate: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth().clickable {
                 onNavigate("lesson_detail/${lesson._id}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),

            horizontalArrangement = Arrangement.spacedBy(10.dp)


        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = lesson.thumbnail,
                    imageLoader = imageLoader
                ),
                contentDescription = "lesson thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(135.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Column(
                modifier = Modifier.fillMaxWidth()

            ) {
                Text(
                    text = lesson.name,
                    color = Grey800,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Lesson ${lesson.lessonNo}", color = Grey600, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = lesson.desc,
            fontSize = 13.sp,
            color = Grey500
        )
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
                    Text("Create Resource")
                }

            }
        }
    }
}
@Composable
fun Lesson(

    imageLoader: ImageLoader,
    lesson: Lesson,
    onClick: () -> Unit
) {


}

