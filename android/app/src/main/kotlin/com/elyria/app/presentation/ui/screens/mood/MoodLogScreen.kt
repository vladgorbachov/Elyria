package com.elyria.app.presentation.ui.screens.mood

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.R
import com.elyria.app.core.base.NavigateBack
import com.elyria.app.core.base.ShowMessage
import com.elyria.app.core.constants.AnimationDurations
import com.elyria.app.domain.model.EmotionCategory
import com.elyria.app.domain.model.MoodTrigger
import com.elyria.app.presentation.ui.components.DisclaimerBanner
import com.elyria.app.presentation.ui.components.MoodSelector
import com.elyria.app.presentation.ui.components.MultiSelectChipRow
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.emotionLabel
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.ui.utils.triggerLabel
import com.elyria.app.presentation.viewmodel.MoodLogUiState
import com.elyria.app.presentation.viewmodel.MoodLogViewModel

@Composable
fun MoodLogScreen(
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoodLogViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is ShowMessage -> snackbarHostState.showSnackbar(event.message.asString(context))
                NavigateBack -> onSaved()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        AnimatedVisibility(
            visible = uiState is MoodLogUiState.Saving,
            enter = fadeIn(androidx.compose.animation.core.tween(AnimationDurations.SHORT)),
            exit = fadeOut(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(Dimens.spacingXl))
                CircularProgressIndicator()
            }
        }

        AnimatedVisibility(
            visible = uiState !is MoodLogUiState.Saving,
            enter = fadeIn(androidx.compose.animation.core.tween(AnimationDurations.SHORT)),
        ) {
            val state = uiState
            val editing = when (state) {
                is MoodLogUiState.Editing -> state
                is MoodLogUiState.Error -> state.previous
                is MoodLogUiState.Success -> MoodLogUiState.Editing(
                    selectedMood = state.entry.moodLevel,
                    note = state.entry.note.orEmpty(),
                    selectedEmotions = state.entry.emotions.toSet(),
                    selectedTriggers = state.entry.triggers.toSet(),
                )
                else -> MoodLogUiState.Editing()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .screenHorizontalPadding()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(Dimens.spacingLg))
                Text(
                    text = stringResource(R.string.mood_log_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingXl))
                MoodSelector(
                    selected = editing.selectedMood,
                    onMoodSelected = viewModel::selectMood,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingXl))
                Text(
                    text = stringResource(R.string.mood_log_emotions_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                Text(
                    text = stringResource(R.string.mood_log_tags_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingMd))
                MultiSelectChipRow(
                    items = EmotionCategory.entries,
                    selected = editing.selectedEmotions,
                    onToggle = viewModel::toggleEmotion,
                    label = { emotionLabel(it) },
                )
                Spacer(modifier = Modifier.height(Dimens.spacingXl))
                Text(
                    text = stringResource(R.string.mood_log_triggers_title),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(Dimens.spacingSm))
                MultiSelectChipRow(
                    items = MoodTrigger.entries.filter { it != MoodTrigger.UNKNOWN },
                    selected = editing.selectedTriggers,
                    onToggle = viewModel::toggleTrigger,
                    label = { triggerLabel(it) },
                )
                Spacer(modifier = Modifier.height(Dimens.spacingXl))
                OutlinedTextField(
                    value = editing.note,
                    onValueChange = viewModel::updateNote,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.mood_log_note_label)) },
                    supportingText = {
                        Text(stringResource(R.string.mood_log_note_counter, editing.note.length))
                    },
                    minLines = 3,
                )
                val errorText = (state as? MoodLogUiState.Error)?.message?.asString(context)
                if (errorText != null) {
                    Spacer(modifier = Modifier.height(Dimens.spacingSm))
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.spacingLg))
                Button(
                    onClick = viewModel::logMood,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = editing.selectedMood != null,
                ) {
                    Text(text = stringResource(R.string.mood_save))
                }
                Spacer(modifier = Modifier.height(Dimens.spacingLg))
                DisclaimerBanner()
            }
        }
    }
}
