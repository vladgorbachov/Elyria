package com.elyria.app.presentation.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.elyria.app.presentation.ui.theme.Dimens

fun Modifier.screenHorizontalPadding(): Modifier {
    return padding(horizontal = Dimens.screenHorizontal)
}

fun Modifier.screenContentPadding(): Modifier {
    return padding(
        PaddingValues(
            horizontal = Dimens.screenHorizontal,
            vertical = Dimens.spacingLg,
        ),
    )
}
