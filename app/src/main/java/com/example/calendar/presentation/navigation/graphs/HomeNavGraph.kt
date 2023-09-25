package com.example.calendar.presentation.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import com.example.calendar.domain.events.Event
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.home.events.EventScreen
import com.example.calendar.presentation.home.notices.NoticesScreen
import com.example.calendar.presentation.home.profile.ProfileScreen
import com.example.calendar.presentation.home.settings.SettingsScreen
import com.example.calendar.presentation.navigation.EventGraph
import com.example.calendar.presentation.navigation.EventRoute

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    eventsState: LazyPagingItems<Event>,
    favoriteEventsState: List<String>,
    onSignOut: () -> Unit,
    googleAuthUiClient: GoogleAuthUiClient,
    onEventFavorite: (String) -> Unit,
    noticesState: LazyPagingItems<Event>,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        route = EventGraph.HOME,
        startDestination = EventRoute.EVENTS,
        modifier = modifier
    ) {
        composable(route = EventRoute.EVENTS) {
            EventScreen(
                events = eventsState,
                favoriteEvents = favoriteEventsState,
                onEventFavorite = onEventFavorite
            )
        }
        composable(route = EventRoute.NOTICES) {
            NoticesScreen(
                notices = noticesState
            )
        }
        composable(route = EventRoute.SETTINGS) {
            SettingsScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = { onSignOut() },
                navigateToProfileScreen = {
                    navController.navigate("profile")
                }
            )
        }
        composable(route = EventRoute.PROFILE) {
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = { onSignOut() }
            )
        }
    }
}