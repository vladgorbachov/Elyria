package com.elyria.app.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elyria.app.presentation.ui.theme.Dimens

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> MultiSelectChipRow(
    items: List<T>,
    selected: Set<T>,
    onToggle: (T) -> Unit,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
    ) {
        items.forEach { item ->
            val isSelected = item in selected
            FilterChip(
                selected = isSelected,
                onClick = { if (enabled) onToggle(item) },
                enabled = enabled,
                label = { Text(label(item)) },
            )
        }
    }
}
