package dev.sanskar.featuretesteduco

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.sanskar.featuretesteduco.core.domain.models.BottomNavBar
import dev.sanskar.featuretesteduco.core.presentation.components.Navigation
import dev.sanskar.featuretesteduco.core.presentation.components.StandardScaffold
import dev.sanskar.featuretesteduco.core.util.Constants
import dev.sanskar.featuretesteduco.core.util.Screen
import dev.sanskar.featuretesteduco.ui.theme.FeatureTestEduCoTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    @Inject
    lateinit var sharedPreferences: SharedPreferences

//    private lateinit var navController: androidx.navigation.NavController

    private lateinit var bottomNavBarItems: BottomNavBar.Items
//    val kare = sharedPreferences.getString(Constants.KEY_ACCOUNT_TYPE, "") ?: ""
private val notificationPermissionLauncher =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        hasNotificationPermissionGranted = isGranted
        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                        showNotificationPermissionRationale()
                    } else {
                        showSettingDialog()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "notification permission granted", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showSettingDialog() {
        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Notification Permission")
            .setMessage("Notification permission is required, Please allow notification permission from setting")
            .setPositiveButton("Ok") { _, _ ->
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showNotificationPermissionRationale() {

        MaterialAlertDialogBuilder(this, com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Alert")
            .setMessage("Notification permission is required, to show notification")
            .setPositiveButton("Ok") { _, _ ->
                if (Build.VERSION.SDK_INT >= 33) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    var hasNotificationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            hasNotificationPermissionGranted = true
        }
        if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

        }
        setContent {
            window.statusBarColor = MaterialTheme.colors.background.toArgb()

            bottomNavBarItems = BottomNavBar.Items(sharedPreferences)

            FeatureTestEduCoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val scaffoldState = rememberScaffoldState()
                    StandardScaffold(
                        navController = navController,
                        state = scaffoldState,
                        showBottomBar = shouldShowBottomBar(navBackStackEntry),
                        modifier = Modifier.fillMaxSize(),
                        onHomeButtonClick = { navigateToHomeScreen(navController) },
                        bottomNavItems = bottomNavBarItems.list,

                    ) {
                        Navigation( imageLoader, navController, scaffoldState,sharedPreferences)
                    }

                }
            }
        }
    }

    private fun shouldShowBottomBar(backStackEntry: NavBackStackEntry?): Boolean {

        var userType2= sharedPreferences.getString(Constants.KEY_ACCOUNT_TYPE,"")?:""

        val routes = if (userType2== "Teacher") {
            listOf(
                Screen.TeacherHomeScreen.route,

                Screen.ProfileScreen.route
            )
        } else {
            listOf(
                Screen.HomeScreen.route,
                Screen.SearchScreen.route,
                Screen.SavedScreen.route,
                Screen.ProfileScreen.route
            )
        }

        val doesRouteMatch = backStackEntry?.destination?.route in routes
        val isOwnProfile = backStackEntry?.destination?.route == "${Screen.ProfileScreen.route}?userId={userId}" &&
                backStackEntry.arguments?.getString("userId") == null

        return doesRouteMatch || isOwnProfile
    }
     private fun navigateToHomeScreen(navController: NavController) {
        val userType = sharedPreferences.getString(Constants.KEY_ACCOUNT_TYPE, "") ?: ""
        val homeRoute = if (userType == "teacher"|| userType == "Teacher") {
            Screen.TeacherHomeScreen.route
        } else {
            Screen.HomeScreen.route
        }
        navController.navigate(homeRoute) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
    }


}
