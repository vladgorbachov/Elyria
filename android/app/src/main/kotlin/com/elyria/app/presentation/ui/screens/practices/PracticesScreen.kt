package com.elyria.app.presentation.ui.screens.practices

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.elyria.app.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.presentation.ui.components.LottiePracticeCard
import com.elyria.app.presentation.ui.theme.Dimens
import com.elyria.app.presentation.ui.utils.screenHorizontalPadding
import com.elyria.app.presentation.viewmodel.PracticesViewModel

@Composable
fun PracticesScreen(
    onPracticeClick: (String) -> Unit,
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
            text = stringResource(R.string.practices_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingLg))
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(Dimens.spacingMd),
        ) {
            items(uiState.practices, key = { it.id }) { practice ->
                LottiePracticeCard(
                    practice = practice,
                    onStart = { onPracticeClick(practice.id) },
                )
            }
        }
    }
}
