package com.elyria.app.presentation.ui.screens.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.R
import com.elyria.app.domain.model.InsightPeriod
import com.elyria.app.presentation.ui.components.MoodTrendChart
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.ui.utils.moodLevelLabel
import com.elyria.app.presentation.ui.utils.moodTrendLabel
import com.elyria.app.presentation.viewmodel.InsightsViewModel

@Composable
fun InsightsScreen(
    onOpenCompanion: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsightsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .screenHorizontalPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = stringResource(R.string.insights_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMd))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        ) {
            FilterChip(
                selected = uiState.period == InsightPeriod.WEEK,
                onClick = { viewModel.selectPeriod(InsightPeriod.WEEK) },
                label = { Text(stringResource(R.string.insights_period_week)) },
            )
            FilterChip(
                selected = uiState.period == InsightPeriod.MONTH,
                onClick = { viewModel.selectPeriod(InsightPeriod.MONTH) },
                label = { Text(stringResource(R.string.insights_period_month)) },
            )
        }
        Spacer(modifier = Modifier.height(Dimens.spacingLg))

        if (uiState.isLoading && uiState.insight == null) {
            CircularProgressIndicator()
            return
        }

        val insight = uiState.insight ?: return
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                Text(text = insight.summary, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = stringResource(
                        R.string.insights_average,
                        "%.1f".format(insight.averageMoodScore),
                        moodTrendLabel(insight.trend),
                    ),
                    style = MaterialTheme.typography.titleMedium,
                )
                insight.mostFrequentMood?.let { mood ->
                    Text(
                        text = stringResource(
                            R.string.insights_most_frequent,
                            moodLevelLabel(mood),
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = Dimens.spacingSm),
                    )
                }
            }
        }
        if (uiState.dailyScores.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Text(
                text = stringResource(R.string.insights_daily_mood),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            MoodTrendChart(points = uiState.dailyScores)
        }
        uiState.patternSummary?.let { patterns ->
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            MoodPatternsCard(summary = patterns)
        }
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        CompanionReflectionCard(
            summary = uiState.aiSummary,
            isLoading = uiState.isAiLoading,
            showError = uiState.showReflectionError,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMd))
        Button(onClick = onOpenCompanion, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.insights_ask_companion))
        }
    }
}
