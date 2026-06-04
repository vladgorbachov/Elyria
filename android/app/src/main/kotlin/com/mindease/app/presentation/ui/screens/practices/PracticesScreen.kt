package com.mindease.app.presentation.ui.screens.practices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindease.app.domain.model.Practice
import com.mindease.app.presentation.ui.theme.Dimens
import com.mindease.app.presentation.ui.utils.screenHorizontalPadding
import com.mindease.app.presentation.viewmodel.PracticesViewModel

@Composable
fun PracticesScreen(
    modifier: Modifier = Modifier,
    viewModel: PracticesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .screenHorizontalPadding(),
    ) {
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        Text(
            text = "Practices",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimens.spacingMd)) {
            items(uiState.practices, key = { it.id }) { practice ->
                PracticeCard(practice = practice)
            }
        }
    }
}

@Composable
private fun PracticeCard(practice: Practice) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.elevationSubtle),
    ) {
        Column(modifier = Modifier.padding(Dimens.cardPadding)) {
            Text(text = practice.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            Text(
                text = practice.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            Text(
                text = "${practice.durationMinutes} min",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
