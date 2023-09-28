package com.example.calendar.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.calendar.R


object EventRoute {
    const val SIGNIN = "SignIn"
    const val EVENTS = "Events"
    const val NOTICES = "Notices"
    const val SETTINGS = "Settings"
    const val SETTINGS_SCREEN = "SettingsScreen"
    const val PROFILE = "Profile"
    const val INFORMATION = "Information"
}

data class EventTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)


class EventNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: EventTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    EventTopLevelDestination(
        route = EventRoute.EVENTS,
        selectedIcon = Icons.Filled.CalendarToday,
        unselectedIcon = Icons.Outlined.CalendarToday,
        iconTextId = R.string.tab_event
    ),
    EventTopLevelDestination(
        route = EventRoute.NOTICES,
        selectedIcon = Icons.Filled.Notifications,
        unselectedIcon = Icons.Outlined.Notifications,
        iconTextId = R.string.tab_notice
    ),
    EventTopLevelDestination(
        route = EventRoute.SETTINGS,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconTextId = R.string.tab_settings
    )
)