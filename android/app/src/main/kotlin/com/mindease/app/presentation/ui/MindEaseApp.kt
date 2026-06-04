package com.mindease.app.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mindease.app.presentation.navigation.MindEaseNavGraph
import com.mindease.app.presentation.ui.theme.MindEaseTheme
import com.mindease.app.presentation.viewmodel.RootViewModel

@Composable
fun MindEaseApp(
    modifier: Modifier = Modifier,
    rootViewModel: RootViewModel = hiltViewModel(),
) {
    val rootState by rootViewModel.uiState.collectAsStateWithLifecycle()

    MindEaseTheme(themeMode = rootState.themeMode) {
        if (rootState.isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MindEaseNavGraph(
                showOnboarding = rootState.showOnboarding,
                modifier = modifier,
            )
        }
    }
}
