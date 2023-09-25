package com.example.calendar.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.window.layout.DisplayFeature
import com.example.calendar.domain.auth.SignInResult
import com.example.calendar.domain.auth.SignInState
import com.example.calendar.domain.events.Event
import com.example.calendar.presentation.home.HomeScreen
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.navigation.graphs.authNavGraph

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    lifecycleScope: androidx.lifecycle.LifecycleCoroutineScope,
    authState: SignInState,
    onSignInResult: (SignInResult) -> Unit,
    onSignOut: () -> Unit,
    authResetState: () -> Unit,
    eventsState: LazyPagingItems<Event>,
    onEventFavorite: (String) -> Unit,
    noticesState: LazyPagingItems<Event>,
) {
    NavHost(
        navController = navController,
        startDestination = EventGraph.AUTH,
        route = EventGraph.ROOT
    ) {
        authNavGraph(
            navController = navController,
            googleAuthUiClient = googleAuthUiClient,
            lifecycleScope = lifecycleScope,
            onSignInResult = onSignInResult,
            authState = authState,
            authResetState = authResetState,
        )
        composable(
            route = EventGraph.HOME,
        ) {
            HomeScreen(
                windowSize = windowSize,
                displayFeatures = displayFeatures,
                googleAuthUiClient = googleAuthUiClient,
                eventsState = eventsState,
                onSignOut = {
                    onSignOut()
                },
                onEventFavorite = onEventFavorite,
                noticesState = noticesState,
            )
        }
    }
}

object EventGraph {
    const val ROOT = "RootGraph"
    const val AUTH = "AuthGraph"
    const val HOME = "HomeGraph"
}