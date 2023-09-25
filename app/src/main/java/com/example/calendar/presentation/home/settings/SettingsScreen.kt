package com.example.calendar.presentation.home.settings


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calendar.R
import com.example.calendar.domain.auth.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsScreen(
    userData: UserData? = UserData(),
    vm: SettingsViewModel = SettingsViewModel(),
    onSignOut: () -> Unit = {},
    navigateToProfileScreen: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
        ) {
            UserCard(userData ?: UserData(), onClick = navigateToProfileScreen)
            Spacer(modifier = Modifier.height(16.dp))

            SettingsGroup(name = "Appearance") {
                SettingsSwitchComp(
                    name = "Dark Mode",
                    icon = R.drawable.baseline_dark_mode_24,
                    iconDesc = "Dark Mode",
                    // value is collected from StateFlow - updates the UI on change
                    state = vm.isSwitchOn.collectAsState()
                ) {
                    // call ViewModel to toggle the value
                    vm.toggleSwitch()
                }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            SettingsClickableComp(
                name = "Sign out",
                icon = R.drawable.outline_logout_24,
                iconDesc = "Sign ",
            ) {
                onSignOut()
            }

        }
    }
}

