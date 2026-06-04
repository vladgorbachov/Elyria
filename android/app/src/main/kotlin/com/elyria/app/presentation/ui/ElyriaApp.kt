package com.elyria.app.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elyria.app.presentation.localization.withAppLanguage
import com.elyria.app.presentation.navigation.ElyriaNavGraph
import com.elyria.app.presentation.ui.theme.ElyriaTheme
import com.elyria.app.presentation.viewmodel.RootViewModel

@Composable
fun ElyriaApp(
    modifier: Modifier = Modifier,
    rootViewModel: RootViewModel = hiltViewModel(),
) {
    val rootState by rootViewModel.uiState.collectAsStateWithLifecycle()
    val baseContext = LocalContext.current
    val localizedContext = remember(baseContext, rootState.appLanguage) {
        baseContext.withAppLanguage(rootState.appLanguage)
    }

    CompositionLocalProvider(LocalContext provides localizedContext) {
        key(rootState.appLanguage) {
            ElyriaTheme(themeMode = rootState.themeMode) {
                if (rootState.isLoading) {
                    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    ElyriaNavGraph(
                        showOnboarding = rootState.showOnboarding,
                        modifier = modifier,
                    )
                }
            }
        }
    }
}
