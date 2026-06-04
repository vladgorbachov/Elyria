package com.mindease.app.presentation.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindease.app.presentation.ui.theme.Dimens
import com.mindease.app.presentation.ui.components.DisclaimerBanner
import com.mindease.app.presentation.viewmodel.OnboardingUiState
import com.mindease.app.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    if (uiState is OnboardingUiState.Done) {
        onFinished()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.sheetPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "MindEase",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = "A calm space to track mood, practice mindfulness, and reflect — privately on your device.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingXl))
        DisclaimerBanner()
        Spacer(modifier = Modifier.height(Dimens.spacingXl))
        Button(
            onClick = { viewModel.completeOnboarding() },
            enabled = uiState !is OnboardingUiState.Saving,
            modifier = Modifier.padding(horizontal = Dimens.spacingMd),
        ) {
            Text(
                text = if (uiState is OnboardingUiState.Saving) "Starting…" else "Get started",
            )
        }
    }
}
