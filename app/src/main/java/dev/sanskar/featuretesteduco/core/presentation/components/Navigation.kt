package dev.sanskar.featuretesteduco.core.presentation.components

import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.feature_auth.presentation.signin.SignInScreen
import dev.sanskar.featuretesteduco.feature_auth.presentation.signup.SignUpScreen


import dev.sanskar.featuretesteduco.feature_auth.presentation.splash.SplashScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.category_list.CreateCourseScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.category_list.PopularCategoryListScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.course_detail.CourseDetailScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.course_detail.LessonDetailScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.course_detail.LessonScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.course_list.MostWatchedCourseListScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.home.CreateLessonScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.home.HomeScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.home.TeacherCourseListScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.home.TeacherHomeScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.saved.SavedScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.search.FilterScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.search.SearchResultScreen
import dev.sanskar.featuretesteduco.feature_course.presentation.search.SearchScreen
import dev.sanskar.featuretesteduco.feature_profile.presentation.profile.ProfileScreen
import dev.sanskar.featuretesteduco.feature_settings.presentation.*

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Navigation(
    imageLoader: ImageLoader,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    sharedPreferences: SharedPreferences
) {


    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {

        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onPopBackStack = navController::popBackStack,
                onNavigate = navController::navigate
            )
        }
        composable(Screen.CreateCourseScreen.route) {
            CreateCourseScreen(
                imageLoader = imageLoader,
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                scaffoldState = scaffoldState
            )
        }
        composable(Screen.TeacherCourseListScreen.route){
            TeacherCourseListScreen(
                imageLoader = imageLoader,
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                scaffoldState = scaffoldState

            )

        }

        composable(Screen.SignInScreen.route) {
            SignInScreen(
                onNavigate = navController::navigate,
                onSignIn = {
                    val userType = sharedPreferences.getString(Constants.KEY_ACCOUNT_TYPE, "") ?: ""
                    navController.popBackStack(route = Screen.SignInScreen.route, inclusive = true)
                    if (userType == "Teacher"|| userType=="TEACHER") {
                        navController.navigate(route = Screen.TeacherHomeScreen.route)
                    } else {
                        navController.navigate(route = Screen.HomeScreen.route)
                    }
                },
                scaffoldState = scaffoldState
            )
        }
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                scaffoldState = scaffoldState,
                onPopBackStack = navController::popBackStack
            )
        }


//


        composable(Screen.HomeScreen.route) {
            HomeScreen(
                onNavigate = navController::navigate,
                imageLoader = imageLoader,
                scaffoldState = scaffoldState
            )
        }
        composable(Screen.TeacherHomeScreen.route) {
            TeacherHomeScreen(imageLoader = imageLoader, scaffoldState = scaffoldState , navController::navigate, sharedPreferences)

        }

        composable(Screen.PopularCategoryListScreen.route) {
            PopularCategoryListScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                imageLoader = imageLoader
            )
        }

        composable(Screen.MostWatchedCourseListScreen.route) {
            MostWatchedCourseListScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                imageLoader = imageLoader,

                )
        }
        composable(
            route = Screen.CreateLessonScreen.route + "/{courseId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                }
            ),
        ){
            val courseId = it.arguments?.getString("courseId")
            println("COURSE ID h ye wla Create wla jiii: ${it.arguments?.getString("courseId")}")
            if (courseId != null) {
                CreateLessonScreen(
                    courseId = courseId,
                    scaffoldState = scaffoldState,
                    onNavigate = navController::navigate,
                    onNavigateUp = navController::navigateUp,
                    imageLoader = imageLoader,

                )
            }

        }



        composable(
            route = Screen.LessonDetailScreen.route + "/{_id}?shouldShowKeyboard={shouldShowKeyboard}",
            arguments = listOf(
                navArgument(
                    name = "_id"
                ) {
                    type = NavType.StringType
                },
                navArgument(
                    name = "shouldShowKeyboard"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
        ) {

            val _id= it.arguments?.getString("_id")
            val shouldShowKeyboard = it.arguments?.getBoolean("shouldShowKeyboard") ?: false
            println("COURSE ID: ${it.arguments?.getString("_id")}")
            LessonDetailScreen(
                _id = _id,
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                scaffoldState = scaffoldState,
                imageLoader = imageLoader,
                shouldShowKeyboard = shouldShowKeyboard
            )
        }
        composable(Screen.LessonScreen.route + "/{courseId}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                }
            )){
            val courseId = it.arguments?.getString("courseId")
            println("COURSE ID: ${it.arguments?.getString("courseId")}")
            if (courseId != null) {
                LessonScreen(

                    onNavigate = navController::navigate,
                    onNavigateUp = navController::navigateUp,
                    imageLoader = imageLoader,
                )
            }
        }


        composable(
            route = Screen.CourseDetailScreen.route + "/{courseId}?shouldShowKeyboard={shouldShowKeyboard}",
            arguments = listOf(
                navArgument(
                    name = "courseId"
                ) {
                    type = NavType.StringType
                },
                navArgument(
                    name = "shouldShowKeyboard"
                ) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
        ) {
            val shouldShowKeyboard = it.arguments?.getBoolean("shouldShowKeyboard") ?: false
            println("COURSE ID: ${it.arguments?.getString("courseId")}")
            CourseDetailScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                scaffoldState = scaffoldState,
                imageLoader = imageLoader,
                shouldShowKeyboard = shouldShowKeyboard,
                sharedPreferences = sharedPreferences

            )
        }

        composable(Screen.SearchScreen.route) {
            SearchScreen(imageLoader = imageLoader, onNavigate = navController::navigate)
        }
//        composable(Screen.SearchResultScreen.route){
//            SearchResultScreen(imageLoader = imageLoader, onNavigate =navController::navigate )
//        }



        composable(Screen.FilterScreen.route) {
            FilterScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp
            )
        }
        composable(Screen.SearchResultScreen.route){
            SearchResultScreen(imageLoader = imageLoader, onNavigate =navController::navigate )
        }

        composable(Screen.SavedScreen.route) {
            SavedScreen(
                imageLoader = imageLoader,
                onNavigate = navController::navigate
            )
        }

        composable(
            route = Screen.ProfileScreen.route + "?userId={userId}",
            arguments = listOf(
                navArgument(name = "userId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            println("USER ID: ${it.arguments?.getString("userId")}")
            val userId = it.arguments?.getString("userId")
            ProfileScreen(
                onNavigate = navController::navigate,
                imageLoader = imageLoader,
                scaffoldState = scaffoldState,
                userId = userId
            )
        }

        composable(
            route = Screen.EditProfileScreen.route,
        ) {
            EditProfileScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                imageLoader = imageLoader,
                scaffoldState = scaffoldState
            )
        }

        composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsScreen(
                onNavigate = navController::navigate,
                onNavigateUp = navController::navigateUp,
                onSignOut = {
                    navController.popBackStack()
                    navController.navigate(route = Screen.SignInScreen.route)
                },
                scaffoldState = scaffoldState
            )
        }

        composable(
            route = Screen.PasswordChangeScreen.route
        ) {
            PasswordChangeScreen(
                onNavigateUp = navController::navigateUp,
                scaffoldState = scaffoldState
            )
        }

        composable(
            route = Screen.PrivacyPolicyScreen.route
        ) {
            PrivacyPolicy(
                navController::navigateUp
            )
        }

        composable(
            route = Screen.TermsAndConditionsScreen.route
        ) {
            TermsAndConditions(
                navController::navigateUp
            )
        }

        composable(
            route = Screen.AboutUs.route
        ) {
            AboutUs(
                navController::navigateUp
            )
        }

    }
}