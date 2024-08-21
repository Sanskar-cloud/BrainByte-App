//package dev.sanskar.featuretesteduco.feature_course.presentation.search
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.Icon
//import androidx.compose.material.Text
//import androidx.compose.material.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.ImageLoader
//import coil.compose.rememberAsyncImagePainter
//import dev.sanskar.featuretesteduco.R
//import dev.sanskar.featuretesteduco.core.domain.models.Course
//import dev.sanskar.featuretesteduco.core.presentation.components.CourseItem
//import dev.sanskar.featuretesteduco.core.util.Screen
//import dev.sanskar.featuretesteduco.ui.theme.Grey500
//import dev.sanskar.featuretesteduco.ui.theme.Grey800
//
//@Composable
//fun SearchResultScreen(
//    viewModel: SearchViewModel = hiltViewModel(),
//    onNavigate: (String) -> Unit,
//    imageLoader: ImageLoader
//) {
//    val searchResults by viewModel.searchResults.collectAsState().value
//    val searchQuery by viewModel.searchQuery.value
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        TextField(
//            value = searchQuery,
//            onValueChange = { viewModel.onSearch(it) },
//            label = { Text("Search") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            items(searchResults) { course ->
//                CourseItem2(course = course, imageLoader = imageLoader, onNavigate = onNavigate)
//            }
//
//        }
//    }
//}
//@Composable
//fun CourseItem2(course: Course, imageLoader: ImageLoader,  onNavigate: (String) -> Unit = {}) {
//    // Your UI representation of a single course
//    Row(
//        modifier = Modifier
//
//            .height(92.dp),
//
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Image(
//            modifier = Modifier
//                .width(width = 80.dp)
//                .fillMaxHeight()
//            ,
//            painter = rememberAsyncImagePainter(
//                model = course.courseThumbnail,
//                imageLoader = imageLoader
//            ),
//            contentDescription = "course thumbnail"
//        )
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Column(modifier = Modifier.clickable {
//            onNavigate(Screen.CourseDetailScreen.route + "/${course._id}")
//        }) {
//            Text(
//                text = course.courseTitle,
//                fontSize = 18.sp,
//                color = Grey800
//            )
//            Spacer(modifier = Modifier.height(6.dp))
//
//            Spacer(modifier = Modifier.height(6.dp))
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(painter = painterResource(id = R.drawable.ic_user_1), contentDescription = null)
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = "${course.noOfStudentEnrolled} student",
//                    fontSize = 12.sp,
//                    color = Grey500
//                )
//                Spacer(modifier = Modifier.width(20.dp))
//                Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, tint = Color(0XffFFA927))
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = "${course.avgRating}",
//                    fontSize = 12.sp,
//                    color = Grey500
//                )
//            }
//            Spacer(modifier = Modifier.height(6.dp))
//
//        }
//        Spacer(modifier = Modifier.width(12.dp))
//        Spacer(modifier = Modifier.width(12.dp))
//        Spacer(modifier = Modifier.width(12.dp))
//        Spacer(modifier = Modifier.width(12.dp))
//        course.price?.let {
//            Text(
//                text = "Rs. $it",
//                fontSize = 18.sp,
//                color = Grey800
//            )
//        }
//
//
//    }
//}