package com.mindease.app.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.mindease.app.R
import com.mindease.app.presentation.ui.theme.Dimens

@Composable
fun DisclaimerBanner(modifier: Modifier = Modifier) {
    val text = stringResource(R.string.disclaimer_short)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = text },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(Dimens.cardPadding),
        )
    }
}
