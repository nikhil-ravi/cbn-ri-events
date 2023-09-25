package com.example.calendar.presentation.home.events

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.ui.theme.CalendarTheme

@Composable
fun DateHeader(
    date: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = date,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

    }
}

@Composable
@Preview
fun LightDateHeaderPreview() {
    CalendarTheme(useDarkTheme = false) {
        DateHeader(date = "2021-01-01")
    }
}

@Composable
@Preview
fun DarkDateHeaderPreview() {
    CalendarTheme(useDarkTheme = true) {
        DateHeader(date = "2021-01-01")
    }
}