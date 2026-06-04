package com.mindease.app.presentation.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindease.app.presentation.ui.components.DisclaimerBanner
import com.mindease.app.presentation.ui.components.MoodSelector
import com.mindease.app.presentation.ui.theme.Dimens
import com.mindease.app.presentation.ui.utils.screenHorizontalPadding
import com.mindease.app.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .screenHorizontalPadding(),
    ) {
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = uiState.greeting,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "${uiState.streak.currentStreak} day streak",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = Dimens.spacingSm),
        )
        Text(
            text = "How are you feeling?",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMd))
        MoodSelector(selected = null, onMoodSelected = {})
        Spacer(modifier = Modifier.height(Dimens.spacingXl))
        DisclaimerBanner()
    }
}
