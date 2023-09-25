package com.example.calendar.presentation.navigation.graphs

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.example.calendar.presentation.auth.SignInScreen
import kotlinx.coroutines.launch
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.calendar.domain.auth.SignInResult
import com.example.calendar.domain.auth.SignInState
import com.example.calendar.presentation.navigation.EventGraph
import com.example.calendar.presentation.navigation.EventRoute

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: LifecycleCoroutineScope,
    onSignInResult: (SignInResult) -> Unit,
    authState: SignInState,
    authResetState: () -> Unit
) {
    val localContext = navController.context
    navigation(
        route = EventGraph.AUTH,
        startDestination = AuthScreen.SignIn.route
    ) {
        composable(EventRoute.SIGNIN) {
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(EventGraph.HOME) {
                        popUpTo(EventGraph.AUTH) {
                            inclusive = true
                        }
                    }
                }
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult =
                                googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                            onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = authState.isSignInSuccessful) {
                if (authState.isSignInSuccessful) {
                    Toast.makeText(
                        localContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(EventGraph.HOME) {
                        popUpTo(EventGraph.AUTH) {
                            inclusive = true
                        }
                    }
                    authResetState()
                }
            }
            SignInScreen(
                state = authState,
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
}

sealed class AuthScreen(val route: String) {
    object SignIn : AuthScreen(route = EventRoute.SIGNIN)
}