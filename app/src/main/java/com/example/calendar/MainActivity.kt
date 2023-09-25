package com.example.calendar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.calendar.domain.auth.SignInResult
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.auth.SignInViewModel
import com.example.calendar.presentation.home.events.EventViewModel
import com.example.calendar.presentation.home.notices.NoticesViewModel
import com.example.calendar.presentation.navigation.EventGraph
import com.example.calendar.presentation.navigation.RootNavigationGraph
import com.example.calendar.ui.theme.CalendarTheme
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(Identity.getSignInClient(applicationContext))
    }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)

                val authViewModel = viewModel<SignInViewModel>()
                val authState by authViewModel.state.collectAsStateWithLifecycle()

                val eventViewModel = hiltViewModel<EventViewModel>()
                val eventState = eventViewModel.eventPagingFlow.collectAsLazyPagingItems()

                val noticesViewModel = hiltViewModel<NoticesViewModel>()
                val noticesState = noticesViewModel.noticePagingFlow.collectAsLazyPagingItems()

                val navController = rememberNavController()

                RootNavigationGraph(
                    navController = navController,
                    googleAuthUiClient = googleAuthUiClient,
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    lifecycleScope = lifecycleScope,
                    authState = authState,
                    onSignInResult = { it: SignInResult -> authViewModel.onSignInResult(it) },
                    onSignOut = {
                        Log.d("MainActivity", "Attempting to sign out")
                        lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                "Signed out",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate(EventGraph.AUTH) {
                                popUpTo(EventGraph.HOME) {
                                    inclusive = true
                                }
                            }
                        }
                    },
                    authResetState = { authViewModel.resetState() },
                    eventsState = eventState,
                    onEventFavorite = { eventId: String ->
                        Log.d("MainActivity", "Attempting to add $eventId to favorites")
                        eventViewModel.addFavorite(
                            googleAuthUiClient.getSignedInUser()?.userId!!,
                            eventId
                        )
                    },
                    noticesState = noticesState,
                )
            }
        }
    }
}