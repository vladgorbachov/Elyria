package com.elyria.app.presentation.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.R
import com.elyria.app.presentation.ui.components.DisclaimerBanner
import com.elyria.app.presentation.ui.components.MoodSelector
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.gardenEmoji
import com.elyria.app.presentation.ui.utils.moodLevelLabel
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogMood: () -> Unit,
    onOpenPractice: (String) -> Unit,
    onOpenCompanion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val greeting = if (uiState.latestMood == null) {
        stringResource(R.string.home_greeting)
    } else {
        stringResource(R.string.home_greeting_with_mood, moodLevelLabel(uiState.latestMood!!))
    }
    val streakLabel = stringResource(R.string.home_day_streak, uiState.streak.currentStreak)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(greeting) })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .screenHorizontalPadding(),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = streakLabel },
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Column(
                    modifier = Modifier.padding(Dimens.cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${uiState.streak.currentStreak}",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = stringResource(R.string.home_day_streak_label),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (uiState.streak.longestStreak > uiState.streak.currentStreak) {
                        Text(
                            text = stringResource(
                                R.string.home_best_streak,
                                uiState.streak.longestStreak,
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
            ) {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Text(
                        text = "${gardenEmoji(uiState.wellbeingProgress.gardenLevel)} ${stringResource(R.string.home_garden_title)}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.home_garden_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.spacingSm),
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Text(
                text = stringResource(R.string.home_quick_mood),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            MoodSelector(
                selected = uiState.latestMood,
                onMoodSelected = {},
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            Button(onClick = onLogMood, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.home_log_mood))
            }
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Card(
                onClick = onOpenCompanion,
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
            ) {
                Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                    Text(
                        text = stringResource(R.string.home_companion_title),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.home_companion_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.spacingSm),
                    )
                    Text(
                        text = stringResource(R.string.home_companion_action),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = Dimens.spacingMd),
                    )
                }
            }
            uiState.featuredPractice?.let { practice ->
                Spacer(modifier = Modifier.height(Dimens.spacingLg))
                Card(
                    onClick = { onOpenPractice(practice.id) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
                ) {
                    Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                        Text(
                            text = stringResource(R.string.home_suggested_practice),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = practice.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "${practice.durationMinutes} min · ${practice.description}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            DisclaimerBanner()
        }
    }
}
