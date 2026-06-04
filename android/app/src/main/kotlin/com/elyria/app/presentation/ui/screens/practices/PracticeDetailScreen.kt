package com.elyria.app.presentation.ui.screens.practices

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elyria.app.R
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.elyria.app.core.constants.AnimationDurations
import com.elyria.app.presentation.ui.components.PracticeTimer
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.viewmodel.PracticeDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PracticeDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val practice = uiState.practice ?: return

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(practice.lottieAsset),
    )
    val buttonScale by animateFloatAsState(
        targetValue = if (uiState.isRunning) 0.96f else 1f,
        animationSpec = androidx.compose.animation.core.tween(AnimationDurations.SHORT),
        label = "buttonScale",
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(practice.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.a11y_back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .screenHorizontalPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(180.dp),
            )
            Text(
                text = practice.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            PracticeTimer(
                secondsRemaining = uiState.secondsRemaining,
                totalSeconds = uiState.totalSeconds,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingLg))

            AnimatedVisibility(
                visible = uiState.isFinished && !uiState.isCompleted,
                enter = fadeIn() + scaleIn(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.practice_great_job),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(Dimens.spacingMd))
                    Button(
                        onClick = { viewModel.completePractice(); onBack() },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(stringResource(R.string.practice_finish))
                    }
                }
            }

            if (!uiState.isFinished || uiState.isCompleted) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
                ) {
                    Button(
                        onClick = {
                            if (uiState.isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .scale(buttonScale),
                    ) {
                        Text(
                            stringResource(
                                if (uiState.isRunning) R.string.practice_pause else R.string.practice_start,
                            ),
                        )
                    }
                    OutlinedButton(
                        onClick = viewModel::resetTimer,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.practice_reset))
                    }
                }
            }
        }
    }
}
