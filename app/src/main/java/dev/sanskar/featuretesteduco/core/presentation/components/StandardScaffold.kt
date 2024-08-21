package dev.sanskar.featuretesteduco.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.sanskar.featuretesteduco.core.domain.models.BottomNavBar
import dev.sanskar.featuretesteduco.core.util.Screen

@Composable
fun StandardScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    onHomeButtonClick: () -> Unit,
    bottomNavItems: List<BottomNavBar>,
    state: ScaffoldState,

    content: @Composable () -> Unit
) {

    Scaffold(
        bottomBar = {
            if (showBottomBar) {


                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    bottomNavItems.forEach{ item ->
                        CustomBottomNavigationItem(
                            item = item,
                            isSelected = navController.currentDestination?.route?.startsWith(item.route) == true
                        ) {
                            if (navController.currentDestination?.route != item.route) {
                                if (item.route == Screen.HomeScreen.route || item.route == Screen.TeacherHomeScreen.route) {
                                    onHomeButtonClick() // Call the callback when home is clicked
                                } else {
                                    navController.navigate(item.route)
                                }
                            }
                        }
                    }
                }
            }
        },
        scaffoldState = state,
        modifier = modifier
    ) {
        content()
    }
}