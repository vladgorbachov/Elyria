package com.elyria.app.presentation.ui.screens.companion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.R
import com.elyria.app.domain.model.CompanionMessageRole
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.viewmodel.CompanionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CompanionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.showWelcomeMessage()
    }

    LaunchedEffect(uiState.messages.size, uiState.isSending) {
        val lastIndex = uiState.messages.lastIndex
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.companion_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.a11y_back),
                        )
                    }
                },
            )
        },
        bottomBar = {
            CompanionInputBar(
                value = uiState.input,
                onValueChange = viewModel::updateInput,
                onSend = viewModel::sendMessage,
                enabled = !uiState.isSending,
                modifier = Modifier.imePadding(),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .screenHorizontalPadding(),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            ) {
                Text(
                    text = stringResource(R.string.companion_disclaimer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(Dimens.spacingSm),
                )
            }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.spacingSm),
            ) {
                items(uiState.messages, key = { it.id }) { message ->
                    CompanionMessageBubble(message = message)
                }
                if (uiState.isSending) {
                    item(key = "reflecting") {
                        Text(
                            text = stringResource(R.string.companion_reflecting),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            uiState.error?.let { error ->
                Text(
                    text = error.asString(context),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun CompanionMessageBubble(
    message: CompanionUiMessage,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val isUser = message.role == CompanionMessageRole.USER
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 320.dp),
            shape = MaterialTheme.shapes.medium,
            color = if (isUser) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ) {
            Text(
                text = message.text.asString(context),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(Dimens.cardPadding),
            )
        }
    }
}

@Composable
private fun CompanionInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.spacingSm),
        verticalAlignment = Alignment.Bottom,
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.companion_input_placeholder)) },
            minLines = 1,
            maxLines = 4,
            enabled = enabled,
        )
        Spacer(modifier = Modifier.padding(Dimens.spacingSm))
        IconButton(onClick = onSend, enabled = enabled) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(R.string.companion_send))
        }
    }
}
