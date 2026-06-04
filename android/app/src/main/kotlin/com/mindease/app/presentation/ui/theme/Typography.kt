package com.mindease.app.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun mindEaseTypography(fontFamily: FontFamily = FontFamily.SansSerif): Typography {
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.displayLarge,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 40.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.headline,
            fontWeight = FontWeight.SemiBold,
        ),
        titleMedium = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.title,
            fontWeight = FontWeight.Medium,
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.body,
            fontWeight = FontWeight.Normal,
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.label,
            fontWeight = FontWeight.Medium,
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontSize = TypeScale.caption,
            fontWeight = FontWeight.Normal,
        ),
    )
}
