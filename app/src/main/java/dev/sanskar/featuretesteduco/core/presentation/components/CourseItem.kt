package dev.sanskar.featuretesteduco.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*


//@Composable
//fun CourseItem(
//    course: CourseOverview,
//    onNavigate: (String) -> Unit = {},
//
//    imageLoader: ImageLoader
//) {
//    var expanded by remember { mutableStateOf(false) }
//    var showDialog by remember { mutableStateOf(false) }
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Delete Course") },
//            text = { Text("Are you sure you want to delete this course?") },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showDialog = false
//
//                    }
//                ) {
//                    Text("Yes")
//                }
//            },
//            dismissButton = {
//                Button(
//                    onClick = { showDialog = false }
//                ) {
//                    Text("No")
//                }
//            }
//        )
//    }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(92.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .weight(1f)
//                .clickable { onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}") }
//        ) {
//            Image(
//                modifier = Modifier
//                    .width(80.dp)
//                    .fillMaxHeight(),
//                painter = rememberAsyncImagePainter(
//                    model = course.courseThumbnailUrl,
//                    imageLoader = imageLoader
//                ),
//                contentDescription = "course thumbnail"
//            )
//            Spacer(modifier = Modifier.width(12.dp))
//            Column {
//                Text(
//                    text = course.courseName,
//                    fontSize = 18.sp,
//                    color = Grey800
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(painter = painterResource(id = R.drawable.ic_user_1), contentDescription = null)
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "${course.noOfStudentEnrolled} student",
//                        fontSize = 12.sp,
//                        color = Grey500
//                    )
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Icon(painter = painterResource(id = R.drawable.ic_star), contentDescription = null, tint = Color(0XffFFA927))
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "${course.rating}",
//                        fontSize = 12.sp,
//                        color = Grey500
//                    )
//                }
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.Top)
//                .padding(end = 8.dp)
//        ) {
//            IconButton(onClick = { expanded = true }) {
//                Icon(painter = painterResource(id = R.drawable.ic_settings), contentDescription = null)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                DropdownMenuItem(onClick = {
//                    expanded = false
//                    showDialog = true
//                }) {
//                    Text("Delete Course")
//                }
//            }
//        }
//    }
//}


@Composable
fun CourseItem(
    course: CourseOverview,
    onNavigate: (String) -> Unit = {},
    imageLoader: ImageLoader
) {
    Row(
        modifier = Modifier

            .height(92.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(width = 80.dp)
                .fillMaxHeight()
                ,
            painter = rememberAsyncImagePainter(
                model = course.courseThumbnailUrl,
                imageLoader = imageLoader
            ),
            contentDescription = "course thumbnail"
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.clickable {
            onNavigate(Screen.CourseDetailScreen.route + "/${course.courseId}")
        }) {
            Text(
                text = course.courseName,
                fontSize = 18.sp,
                color = Grey800
            )
            Spacer(modifier = Modifier.height(6.dp))

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
            Spacer(modifier = Modifier.height(6.dp))

        }
        Spacer(modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier.width(12.dp))
        course.price?.let {
            Text(
                text = "Rs. $it",
                fontSize = 18.sp,
                color = Grey800
            )
        }


    }


}
