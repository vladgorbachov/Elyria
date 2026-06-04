package com.mindease.app.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import com.mindease.app.core.constants.MoodColors
import com.mindease.app.domain.model.MoodLevel
import com.mindease.app.presentation.ui.theme.ColorPalette
import com.mindease.app.presentation.ui.theme.Dimens

private data class MoodOption(
    val level: MoodLevel,
    val emoji: String,
    val color: Color,
    val label: String,
)

private val moodOptions = listOf(
    MoodOption(MoodLevel.VERY_LOW, "😔", MoodColors.lavender, "Very low"),
    MoodOption(MoodLevel.LOW, "😕", MoodColors.lavender.copy(alpha = 0.8f), "Low"),
    MoodOption(MoodLevel.NEUTRAL, "😐", ColorPalette.TextSecondaryLight, "Neutral"),
    MoodOption(MoodLevel.GOOD, "🙂", MoodColors.beige, "Good"),
    MoodOption(MoodLevel.GREAT, "😊", MoodColors.growth, "Great"),
)

@Composable
fun MoodSelector(
    selected: MoodLevel?,
    onMoodSelected: (MoodLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMd),
        contentPadding = PaddingValues(horizontal = Dimens.spacingSm),
    ) {
        items(moodOptions, key = { it.level.name }) { option ->
            val isSelected = selected == option.level
            Box(
                modifier = Modifier
                    .size(Dimens.moodCircleSize)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        } else {
                            option.color.copy(alpha = 0.35f)
                        },
                    )
                    .clickable { onMoodSelected(option.level) }
                    .semantics {
                        role = Role.Button
                        contentDescription = option.label
                        this.selected = isSelected
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = option.emoji,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}
