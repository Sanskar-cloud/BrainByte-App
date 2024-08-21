package dev.sanskar.featuretesteduco.feature_course.presentation.saved

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import dev.sanskar.featuretesteduco.core.presentation.components.CategoryCard
import dev.sanskar.featuretesteduco.core.presentation.components.CourseItem
import dev.sanskar.featuretesteduco.core.presentation.components.CourseOverviewCard
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.Grey50
import dev.sanskar.featuretesteduco.ui.theme.SpaceLarge


@Composable
fun SavedScreen(
    imageLoader: ImageLoader,
    onNavigate: (String) -> Unit,
    viewModel: SavedViewModel = hiltViewModel()
) {

    val bookmarkState = viewModel.bookmarkedCourses.collectAsState().value
    val CoursePagingState=viewModel.CoursePagingState.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text("My Save List", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(50.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {

                    LazyRow{
                        items(CoursePagingState.items.size) { i ->
                            val course= CoursePagingState.items[i]
                            if (i >= CoursePagingState.items.size - 1 &&!CoursePagingState.endReached&& !CoursePagingState.isLoading) {
                                viewModel.loadCoursesForTchr()
                                println("vfdvdbdbdb")
                            }
                            CourseOverviewCard(
                                course = course,
                                imageLoader = imageLoader,
                                onClick = {
                                    onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}")
                                })



                            if (i < CoursePagingState.items.size - 1) {
                                Spacer(modifier = Modifier.width(SpaceLarge))
                            }
                        }
                    }

                }

                }


//            bookmarkState.courses?.let {
//                items(it) { course ->
//                    CourseItem(
//                        course = course,
//                        imageLoader = imageLoader
//                    )
//                }
//            }

            item {
                Button(
                    onClick = { onNavigate(Screen.MostWatchedCourseListScreen.route) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Grey50)
                ) {
                    Text(
                        text = "Add More",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

        }

    }

}

