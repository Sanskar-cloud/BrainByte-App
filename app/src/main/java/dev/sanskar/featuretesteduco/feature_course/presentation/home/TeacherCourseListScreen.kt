package dev.sanskar.featuretesteduco.feature_course.presentation.home

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.presentation.components.CourseItem

import dev.sanskar.featuretesteduco.core.presentation.components.CourseItemForTeacher
import dev.sanskar.featuretesteduco.core.presentation.components.CourseOverviewCard
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.Grey900
import dev.sanskar.featuretesteduco.ui.theme.SpaceLarge
import dev.sanskar.featuretesteduco.ui.theme.title2new

@Composable
fun TeacherCourseListScreen(
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit,
    imageLoader: ImageLoader,
    scaffoldState: ScaffoldState,

    viewModel: TeacherHomeViewModel = hiltViewModel(),
    vieModel2:LessonViewmodel = hiltViewModel()
) {

    val CoursePagingState=viewModel.CoursePagingState.value

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onNavigateUp() },
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_user ), contentDescription = null)
            }
            Text(text = "All Courses", color = Grey900, style = title2new)

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {
            items(CoursePagingState.items.size) { i ->
                val course = CoursePagingState.items[i]
                if (i >= CoursePagingState.items.size - 1 && !CoursePagingState.endReached && !CoursePagingState.isLoading)
                {
//                    println(sharedPreferences.getString(Constants.KEY_USER_ID,""))
                    viewModel.loadCoursesForTchr()
                }
                CourseItemForTeacher(
                    course = course,
                    imageLoader = imageLoader,
                    onNavigate = onNavigate,
                    scaffoldState = scaffoldState
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
    }


}