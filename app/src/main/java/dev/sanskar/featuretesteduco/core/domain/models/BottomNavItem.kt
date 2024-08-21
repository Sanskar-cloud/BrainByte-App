package dev.sanskar.featuretesteduco.core.domain.models

import android.content.SharedPreferences
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import dev.sanskar.featuretesteduco.R
import dev.sanskar.featuretesteduco.core.util.Constants


import dev.sanskar.featuretesteduco.core.util.Screen
import javax.inject.Inject

data class BottomNavItem(
    val route: String,
    val icon: ImageVector? = null,
    val contentDescription: String? = null,
    val alertCount: Int? = null
)


sealed class BottomNavBar(
    @DrawableRes val icon: Int,
    val title: String,
    val route: String
) {
    object Home : BottomNavBar(
        icon = R.drawable.ic_home,
        title = "Home",
        route = Screen.HomeScreen.route
    )
    object THome : BottomNavBar(
        icon = R.drawable.ic_home,
        title = "Home",
        route = Screen.TeacherHomeScreen.route
    )
    object Search : BottomNavBar(
        icon = R.drawable.ic_search,
        title = "Search",
        route = Screen.SearchScreen.route
    )
    object Saved : BottomNavBar(
        icon = R.drawable.ic_saved,
        title = "Saved",
        route = Screen.SavedScreen.route
    )
    object Profile : BottomNavBar(
        icon = R.drawable.ic_profile,
        title = "Profile",
        route = Screen.ProfileScreen.route
    )
    class Items @Inject constructor(private val sharedPreferences: SharedPreferences) {
        val list: List<BottomNavBar>
            get() {
                val userType = sharedPreferences.getString(Constants.KEY_ACCOUNT_TYPE, "") ?: ""
                return if (userType == "TEACHER" || userType == "Teacher") {
                    listOf(THome, Profile)
                } else {
                    listOf(Home,  Search, Saved,Profile)
                }
            }
    }
//    object Items {
//
//        val list = listOf(
//            Home,
//            Search,
//            Saved,
//            Profile
//        )
//    }


}
