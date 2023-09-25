package com.example.calendar.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.LazyPagingItems
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.calendar.domain.events.Event
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.navigation.EventBottomNavigationBar
import com.example.calendar.presentation.navigation.EventNavigationRail
import com.example.calendar.presentation.navigation.EventRoute
import com.example.calendar.presentation.navigation.EventTopLevelDestination
import com.example.calendar.presentation.navigation.ModalNavigationDrawerContent
import com.example.calendar.presentation.navigation.PermanentNavigationDrawerContent
import com.example.calendar.presentation.navigation.graphs.HomeNavGraph
import com.example.calendar.presentation.utils.DevicePosture
import com.example.calendar.presentation.utils.EventContentType
import com.example.calendar.presentation.utils.EventNavigationContentPosition
import com.example.calendar.presentation.utils.EventNavigationType
import com.example.calendar.presentation.utils.isBookPosture
import com.example.calendar.presentation.utils.isSeparating
import kotlinx.coroutines.launch

fun getNavigationStyleAndContentPosition(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>
): Triple<EventNavigationType, EventContentType, EventNavigationContentPosition> {
    val navigationType: EventNavigationType
    val contentType: EventContentType


    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)
        isSeparating(foldingFeature) -> DevicePosture.Separating(
            foldingFeature.bounds,
            foldingFeature.orientation
        )

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = EventNavigationType.BOTTOM_NAVIGATION
            contentType = EventContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = EventNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                EventContentType.DUAL_PANE
            } else {
                EventContentType.SINGLE_PANE
            }
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                EventNavigationType.NAVIGATION_RAIL
            } else {
                EventNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = EventContentType.DUAL_PANE
        }

        else -> {
            navigationType = EventNavigationType.BOTTOM_NAVIGATION
            contentType = EventContentType.SINGLE_PANE
        }
    }

    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            EventNavigationContentPosition.TOP
        }

        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            EventNavigationContentPosition.CENTER
        }

        else -> {
            EventNavigationContentPosition.TOP
        }
    }

    return Triple(navigationType, contentType, navigationContentPosition)
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    googleAuthUiClient: GoogleAuthUiClient,
    eventsState: LazyPagingItems<Event>,
    favoriteEventsState: List<String>,
    onSignOut: () -> Unit,
    onEventFavorite: (String) -> Unit,
    noticesState: LazyPagingItems<Event>,
) {
    val (
        navigationType,
        contentType,
        navigationContentPosition
    ) = getNavigationStyleAndContentPosition(windowSize, displayFeatures)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: EventRoute.EVENTS
    val navigateToTopLevelDestination = { destination: EventTopLevelDestination ->
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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (navigationType == EventNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
            )
        }) {
            HomeContent(
                modifier = modifier,
                navigationType = navigationType,
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                navController = navController,
                eventsState = eventsState,
                favoriteEventsState = favoriteEventsState,
                onSignOut = onSignOut,
                googleAuthUiClient = googleAuthUiClient,
                onEventFavorite = onEventFavorite,
                noticesState = noticesState,
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            HomeContent(
                modifier = modifier,
                navigationType = navigationType,
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                navController = navController,
                eventsState = eventsState,
                favoriteEventsState = favoriteEventsState,
                onSignOut = onSignOut,
                googleAuthUiClient = googleAuthUiClient,
                onEventFavorite = onEventFavorite,
                noticesState = noticesState
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier,
    navigationType: EventNavigationType,
    selectedDestination: String,
    navigationContentPosition: EventNavigationContentPosition,
    navigateToTopLevelDestination: (EventTopLevelDestination) -> Unit,
    navController: NavHostController,
    eventsState: LazyPagingItems<Event>,
    favoriteEventsState: List<String>,
    onSignOut: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    onEventFavorite: (String) -> Unit,
    noticesState: LazyPagingItems<Event>,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == EventNavigationType.NAVIGATION_RAIL) {
            EventNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            HomeNavGraph(
                navController = navController,
                eventsState = eventsState,
                favoriteEventsState = favoriteEventsState,
                onSignOut = { onSignOut() },
                googleAuthUiClient = googleAuthUiClient,
                onEventFavorite = onEventFavorite,
                noticesState = noticesState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
            AnimatedVisibility(visible = navigationType == EventNavigationType.BOTTOM_NAVIGATION) {
                EventBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

