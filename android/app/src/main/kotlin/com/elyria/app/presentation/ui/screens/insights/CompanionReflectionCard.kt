package com.elyria.app.presentation.ui.screens.insights

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.elyria.app.R
import com.elyria.app.presentation.ui.theme.Dimens

@Composable
fun CompanionReflectionCard(
    summary: String?,
    isLoading: Boolean,
    showError: Boolean,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(R.string.elyria_reflection_title)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = title },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSubtle),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ),
    ) {
        Column(modifier = Modifier.padding(Dimens.cardPadding)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            when {
                isLoading -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(Dimens.spacingXl))
                        Spacer(modifier = Modifier.size(Dimens.spacingMd))
                        Text(
                            text = stringResource(R.string.elyria_reflection_loading),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                !summary.isNullOrBlank() -> {
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.semantics {
                            contentDescription = summary
                        },
                    )
                }
                else -> {
                    val fallback = if (showError) {
                        stringResource(R.string.elyria_reflection_error)
                    } else {
                        stringResource(R.string.elyria_reflection_empty)
                    }
                    Text(
                        text = fallback,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
