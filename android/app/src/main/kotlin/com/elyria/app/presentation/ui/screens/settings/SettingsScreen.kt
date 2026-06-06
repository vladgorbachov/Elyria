package com.elyria.app.presentation.ui.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.R
import com.elyria.app.core.base.LaunchExportDocument
import com.elyria.app.domain.model.AppLanguage
import com.elyria.app.domain.model.AppThemeMode
import com.elyria.app.presentation.ui.components.DisclaimerBanner
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.viewmodel.SettingsMessage
import com.elyria.app.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var pendingExportJson by remember { mutableStateOf<String?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) { uri: Uri? ->
        val json = pendingExportJson
        pendingExportJson = null
        if (uri == null || json == null) {
            if (json != null) {
                viewModel.onExportWriteResult(success = false)
            }
            return@rememberLauncherForActivityResult
        }
        val success = writeJsonToUri(context.contentResolver, uri, json)
        viewModel.onExportWriteResult(success)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is LaunchExportDocument -> {
                    pendingExportJson = event.json
                    exportLauncher.launch(event.fileName)
                }
            }
        }
    }

    LaunchedEffect(uiState.message) {
        val message = uiState.message ?: return@LaunchedEffect
        val text = when (message) {
            SettingsMessage.ExportSuccess -> context.getString(R.string.settings_export_success)
            SettingsMessage.ExportFailed -> context.getString(R.string.settings_export_failure)
            SettingsMessage.ExportNoData -> context.getString(R.string.settings_export_no_data)
            SettingsMessage.DeleteSuccess -> context.getString(R.string.settings_delete_success)
            SettingsMessage.DeleteFailed -> context.getString(R.string.settings_delete_failure)
            SettingsMessage.ThemeUpdateFailed -> context.getString(R.string.settings_theme_failure)
            SettingsMessage.LanguageUpdateFailed -> context.getString(R.string.settings_language_failure)
        }
        snackbarHostState.showSnackbar(text)
        viewModel.clearMessage()
    }

    if (uiState.showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = viewModel::dismissDeleteConfirmation,
            title = { Text(stringResource(R.string.settings_delete_confirm_title)) },
            text = { Text(stringResource(R.string.settings_delete_confirm_body)) },
            confirmButton = {
                TextButton(onClick = viewModel::deleteAllData) {
                    Text(stringResource(R.string.settings_delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissDeleteConfirmation) {
                    Text(stringResource(R.string.settings_delete_cancel))
                }
            },
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .screenHorizontalPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(Dimens.spacingLg))
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingXl))

            SettingsSectionTitle(stringResource(R.string.settings_appearance))
            ThemeModeCard(
                selected = uiState.themeMode,
                onSelected = viewModel::onThemeSelected,
            )

            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            SettingsSectionTitle(stringResource(R.string.settings_language_title))
            LanguageModeCard(
                selected = uiState.appLanguage,
                onSelected = viewModel::onLanguageSelected,
            )

            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            SettingsSectionTitle(stringResource(R.string.settings_data))
            DataActionsCard(
                isExporting = uiState.isExporting,
                isDeleting = uiState.isDeleting,
                onExport = viewModel::prepareExport,
                onDelete = viewModel::requestDeleteConfirmation,
            )

            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            SettingsSectionTitle(stringResource(R.string.settings_safety))
            Text(
                text = stringResource(R.string.settings_privacy_note),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            DisclaimerBanner()
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            OutlinedButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.settings_crisis_placeholder))
            }
            Spacer(modifier = Modifier.height(Dimens.spacingXl))
        }
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(Dimens.spacingMd))
}

@Composable
private fun ThemeModeCard(
    selected: AppThemeMode,
    onSelected: (AppThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(Dimens.cardPadding),
        ) {
            Text(
                text = stringResource(R.string.settings_theme),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            ThemeOption(
                label = stringResource(R.string.settings_theme_system),
                mode = AppThemeMode.SYSTEM,
                selected = selected,
                onSelected = onSelected,
            )
            ThemeOption(
                label = stringResource(R.string.settings_theme_light),
                mode = AppThemeMode.LIGHT,
                selected = selected,
                onSelected = onSelected,
            )
            ThemeOption(
                label = stringResource(R.string.settings_theme_dark),
                mode = AppThemeMode.DARK,
                selected = selected,
                onSelected = onSelected,
            )
        }
    }
}

@Composable
private fun LanguageModeCard(
    selected: AppLanguage,
    onSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(Dimens.cardPadding),
        ) {
            LanguageOption(
                label = stringResource(R.string.settings_language_system),
                language = AppLanguage.SYSTEM,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_english),
                language = AppLanguage.ENGLISH,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_russian),
                language = AppLanguage.RUSSIAN,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_ukrainian),
                language = AppLanguage.UKRAINIAN,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_romanian),
                language = AppLanguage.ROMANIAN,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_spanish),
                language = AppLanguage.SPANISH,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_german),
                language = AppLanguage.GERMAN,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_french),
                language = AppLanguage.FRENCH,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_portuguese_br),
                language = AppLanguage.PORTUGUESE_BR,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_portuguese),
                language = AppLanguage.PORTUGUESE,
                selected = selected,
                onSelected = onSelected,
            )
            LanguageOption(
                label = stringResource(R.string.settings_language_dutch),
                language = AppLanguage.DUTCH,
                selected = selected,
                onSelected = onSelected,
            )
        }
    }
}

@Composable
private fun LanguageOption(
    label: String,
    language: AppLanguage,
    selected: AppLanguage,
    onSelected: (AppLanguage) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected == language,
                onClick = { onSelected(language) },
                role = Role.RadioButton,
            )
            .padding(vertical = Dimens.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected == language, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = Dimens.spacingMd),
        )
    }
}

@Composable
private fun ThemeOption(
    label: String,
    mode: AppThemeMode,
    selected: AppThemeMode,
    onSelected: (AppThemeMode) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected == mode,
                onClick = { onSelected(mode) },
                role = Role.RadioButton,
            )
            .padding(vertical = Dimens.spacingSm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected == mode,
            onClick = null,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = Dimens.spacingMd),
        )
    }
}

@Composable
private fun DataActionsCard(
    isExporting: Boolean,
    isDeleting: Boolean,
    onExport: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(Dimens.elevationSubtle),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onExport,
                enabled = !isExporting && !isDeleting,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isExporting) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(Dimens.spacingLg),
                        strokeWidth = Dimens.spacingSm,
                    )
                } else {
                    Text(stringResource(R.string.settings_export_data))
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            OutlinedButton(
                onClick = onDelete,
                enabled = !isExporting && !isDeleting,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(Dimens.spacingLg),
                        strokeWidth = Dimens.spacingSm,
                    )
                } else {
                    Text(stringResource(R.string.settings_delete_all_data))
                }
            }
        }
    }
}

private fun writeJsonToUri(
    contentResolver: android.content.ContentResolver,
    uri: Uri,
    json: String,
): Boolean {
    return try {
        contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(json.toByteArray(Charsets.UTF_8))
            true
        } ?: false
    } catch (_: Exception) {
        false
    }
}
