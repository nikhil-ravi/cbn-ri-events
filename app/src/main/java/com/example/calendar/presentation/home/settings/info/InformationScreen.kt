package com.example.calendar.presentation.home.settings.info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.calendar.ui.theme.CalendarTheme


@Composable
fun InformationScreen() {
    val servicesAndPrograms = listOf(
        "Weekday congregate luncheon meals",
        "Exercise, dance, and yoga classes",
        "Computer training",
        "Health and wellness activities",
        "Arts programs",
        "Holiday parties",
        "Other activities",
        "Case assistance for government benefit programs",
        "Addressing landlord/tenant disputes",
        "Accessing medical care",
        "Weekday meal delivery to homebound individuals on Roosevelt Island"
    )

    val infoText = """
        The Roosevelt Island Older Adult Program provides a variety of socialization, recreation, and education programs including:
    """.trimIndent()

    Scaffold (
        topBar = {
            Text(
                text = "Roosevelt Island Older Adult Program",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = infoText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            BulletPointsList(items = servicesAndPrograms)
            ContactInformation()
        }
    }

}

@Composable
fun BulletPointsList(items: List<String>) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Services and Programs:",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        items.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun ContactInformation() {
    val contactName = "Lisa Fernandez"
    val contactEmail = "fernandezl@carterburdennetwork.org"
    val contactPhoneNumber = "212-980-1888"
    val address = "546 Main Street, Roosevelt Island\nNew York, NY 10044"
    val hoursOfOperation = "Monday - Friday: 9:00am - 5:00pm"

    val localContext = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Contact Information",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Contact: $contactName",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        ClickableText(
            text = AnnotatedString("Phone: $contactPhoneNumber"),
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$contactPhoneNumber")
                startActivity(localContext, intent, null)
            },
            modifier = Modifier.clickable { },
            style = MaterialTheme.typography.bodyMedium
                .copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textDecoration = TextDecoration.Underline
                ),
        )
        ClickableText(
            text = AnnotatedString("Email: $contactEmail"),
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:$contactEmail")
                startActivity(localContext, intent, null)
            },
            modifier = Modifier.clickable { },
            style = MaterialTheme.typography.bodyMedium
                    .copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline
                    ),
        )
        Text(
            text = "Location:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = hoursOfOperation,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Preview
@Composable
fun InformationScreenPreview() {
    CalendarTheme (true) {
        InformationScreen()
    }
}