package com.elyria.app.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import com.elyria.app.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.elyria.app.domain.model.Practice
import com.elyria.app.presentation.ui.theme.Dimens

@Composable
fun LottiePracticeCard(
    practice: Practice,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(practice.lottieAsset))
    val cardLabel = stringResource(
        R.string.practice_card_a11y,
        practice.title,
        practice.durationMinutes,
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = cardLabel },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSubtle),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(120.dp),
            )
            Column(modifier = Modifier.padding(Dimens.cardPadding)) {
                Text(text = practice.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                Text(
                    text = practice.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                Text(
                    text = stringResource(R.string.practice_minutes, practice.durationMinutes),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
                    Text(text = stringResource(R.string.practice_start))
                }
            }
        }
    }
}
