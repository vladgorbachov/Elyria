package com.elyria.app.presentation.ui.screens.insights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elyria.app.R
import com.elyria.app.domain.model.MoodPatternSummary
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.emotionLabel
import com.elyria.app.presentation.ui.utils.triggerLabel

@Composable
fun MoodPatternsCard(
    summary: MoodPatternSummary,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
    ) {
        Column(modifier = Modifier.padding(Dimens.cardPadding)) {
            Text(
                text = stringResource(R.string.insights_patterns_title),
                style = MaterialTheme.typography.titleMedium,
            )
            if (summary.entryCount == 0) {
                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                Text(
                    text = stringResource(R.string.insights_patterns_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                return@Column
            }
            if (summary.topEmotions.isNotEmpty()) {
                val emotionLabels = summary.topEmotions.map { emotionLabel(it) }
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = stringResource(R.string.insights_top_emotions),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = emotionLabels.joinToString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.spacingSm),
                )
            }
            if (summary.topTriggers.isNotEmpty()) {
                val triggerLabels = summary.topTriggers.map { triggerLabel(it) }
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = stringResource(R.string.insights_top_triggers),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = triggerLabels.joinToString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.spacingSm),
                )
            }
            if (summary.lowMoodTriggers.isNotEmpty()) {
                val lowTriggerLabels = summary.lowMoodTriggers.map { triggerLabel(it) }
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Text(
                    text = stringResource(R.string.insights_low_mood_triggers),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = lowTriggerLabels.joinToString(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.spacingSm),
                )
            }
        }
    }
}
