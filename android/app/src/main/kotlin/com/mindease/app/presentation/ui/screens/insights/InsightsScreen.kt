package com.mindease.app.presentation.ui.screens.insights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindease.app.presentation.ui.theme.Dimens
import com.mindease.app.presentation.ui.utils.screenHorizontalPadding

@Composable
fun InsightsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .screenHorizontalPadding(),
    ) {
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = "Mood charts and on-device summaries will appear here.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
