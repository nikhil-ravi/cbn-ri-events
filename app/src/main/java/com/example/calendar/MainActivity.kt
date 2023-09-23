package com.example.calendar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.auth.SignInScreen
import com.example.calendar.presentation.auth.SignInViewModel
import com.example.calendar.presentation.events.EventScreen
import com.example.calendar.presentation.events.EventViewModel
import com.example.calendar.presentation.profile.ProfileScreen
import com.example.calendar.presentation.settings.SettingsScreen
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
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "auth") {
                    navigation(
                        route = "auth",
                        startDestination = "sign_in"
                    ) {
                        composable("sign_in") {
                            val authViewModel = viewModel<SignInViewModel>()
                            val state by authViewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("events") {
                                        popUpTo("auth") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult =
                                                googleAuthUiClient.signInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )
                                            authViewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("events")
                                    authViewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )

                        }
                    }

                    navigation(
                        route = "events",
                        startDestination = "eventsScreen"
                    ) {
                        composable("eventsScreen") {
                            val eventViewModel = hiltViewModel<EventViewModel>()
                            val events = eventViewModel.eventPagingFlow.collectAsLazyPagingItems()
                            EventScreen(events = events)
                        }
                    }

                    navigation(
                        route = "settings",
                        startDestination = "settingsScreen"
                    ) {
                        composable("settingsScreen") {
                            SettingsScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    Log.d("MainActivity", "Attempting to sign out")
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("auth") {
                                            popUpTo("settings") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                },
                                navigateToProfileScreen = {
                                    navController.navigate("profile")
                                }
                            )
                        }

                        composable("profile") {
//                                val settingsViewModel =
//                                    it.sharedViewModel<SettingsViewModel>(navController)
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    Log.d("MainActivity", "Attempting to sign out")
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "Signed out",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navController.navigate("auth") {
                                            popUpTo("settings") {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}