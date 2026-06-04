package com.elyria.app.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.elyria.app.presentation.viewmodel.DailyMoodPoint

@Composable
fun MoodTrendChart(
    points: List<DailyMoodPoint>,
    modifier: Modifier = Modifier,
) {
    if (points.isEmpty()) return

    val barColor = MaterialTheme.colorScheme.primary
    val maxScore = 5f

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
    ) {
        val barWidth = size.width / (points.size * 2f)
        val gap = barWidth
        points.forEachIndexed { index, point ->
            val barHeight = (point.averageScore / maxScore) * size.height
            val left = index * (barWidth + gap) + gap / 2f
            drawRoundRect(
                color = barColor,
                topLeft = Offset(left, size.height - barHeight),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f),
            )
        }
    }
}
