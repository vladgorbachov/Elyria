package com.elyria.app.presentation.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.elyria.app.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.core.constants.AnimationDurations
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.components.DisclaimerBanner
import com.elyria.app.presentation.viewmodel.OnboardingUiState
import com.elyria.app.presentation.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    if (uiState is OnboardingUiState.Done) {
        onFinished()
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(androidx.compose.animation.core.tween(AnimationDurations.MEDIUM)) +
            slideInVertically { it / 4 },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.screenHorizontal)
                .padding(vertical = Dimens.sheetPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Text(
                text = stringResource(R.string.onboarding_tagline),
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
            ) {
                Text(
                    text = if (uiState is OnboardingUiState.Saving) {
                        stringResource(R.string.onboarding_starting)
                    } else {
                        stringResource(R.string.onboarding_get_started)
                    },
                )
            }
        }
    }
}
