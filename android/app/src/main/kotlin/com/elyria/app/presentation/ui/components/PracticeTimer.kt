package com.elyria.app.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import com.elyria.app.core.constants.AnimationDurations
import com.elyria.app.presentation.ui.theme.Dimens

@Composable
fun PracticeTimer(
    secondsRemaining: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier,
) {
    val progress = if (totalSeconds == 0) 0f else secondsRemaining.toFloat() / totalSeconds
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(AnimationDurations.SHORT),
        label = "timerProgress",
    )
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val label = String.format("%02d:%02d remaining", minutes, seconds)

    Column(
        modifier = modifier.semantics { contentDescription = label },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMd),
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(160.dp),
            strokeWidth = Dimens.spacingSm,
        )
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.displayLarge,
        )
    }
}
