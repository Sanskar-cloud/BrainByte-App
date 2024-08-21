package dev.sanskar.featuretesteduco.core.util

sealed class Screen(val route: String) {

    //Auth
    object SplashScreen : Screen("splash_screen")
    object LessonScreen : Screen("lessons_screen")
    object CreateLessonScreen : Screen("create_lesson_screen")

    object TeacherHomeScreen : Screen("teacher_home_screen")
    object CreateCourseScreen : Screen("create_course_screen")
    object LessonDetailScreen : Screen("lesson_detail")


    object TeacherCourseListScreen : Screen("teacher_course_list_screen")

    object SignInScreen : Screen("login_screen")
    object SignUpScreen : Screen("register_screen")

    //Home And Course
    object HomeScreen : Screen("home_screen")
    object PopularCategoryListScreen: Screen("popular_cate_list_screen")
    object MostWatchedCourseListScreen: Screen("most_watched_course_list_screen")
    object SearchScreen : Screen("search_screen")
    object SavedScreen: Screen("bookmark_screen")
    object FilterScreen: Screen("filter_screen")
    object SearchResultScreen: Screen("search_result_screen")
    object CategoryDetailScreen : Screen("category_detail_screen")
    object CourseDetailScreen : Screen("course_detail_screen")

    //Profile
    object ProfileScreen : Screen("profile_screen")
    object EditProfileScreen : Screen("edit_profile_screen")
    object PersonListScreen : Screen("person_list_screen")

    //Settings
    object SettingsScreen: Screen("settings_screen")
    object PasswordChangeScreen: Screen("password_change_screen")
    object PrivacyPolicyScreen: Screen("privacy_policy_screen")
    object TermsAndConditionsScreen: Screen("terms_conditions_screen")
    object AboutUs: Screen("about_us_screen")

}