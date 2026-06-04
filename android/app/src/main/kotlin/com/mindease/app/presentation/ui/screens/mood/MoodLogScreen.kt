package com.mindease.app.presentation.ui.screens.mood

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindease.app.presentation.ui.theme.Dimens
import com.mindease.app.presentation.ui.utils.screenHorizontalPadding
import com.mindease.app.presentation.ui.components.MoodSelector
import com.mindease.app.presentation.viewmodel.MoodLogUiState
import com.mindease.app.presentation.viewmodel.MoodLogViewModel

@Composable
fun MoodLogScreen(
    modifier: Modifier = Modifier,
    viewModel: MoodLogViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val editing = uiState as? MoodLogUiState.Editing ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .screenHorizontalPadding(),
    ) {
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = "Log your mood",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingXl))
        MoodSelector(
            selected = editing.selectedMood,
            onMoodSelected = viewModel::selectMood,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingXl))
        OutlinedTextField(
            value = editing.note,
            onValueChange = viewModel::updateNote,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Optional note") },
            minLines = 4,
        )
    }
}
