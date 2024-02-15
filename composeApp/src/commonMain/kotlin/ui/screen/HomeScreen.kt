package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.EtongAppDI
import viewmodel.HomeScreenModel

class HomeScreen: Screen {

    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = rememberScreenModel { EtongAppDI.homeScreenModel }
        val state = remember { screenModel.state }

        Box (modifier = Modifier.fillMaxSize().background(Color.Transparent)) {
            when (val currentState = state.value) {
                is HomeScreenModel.HomeScreenState.Success -> {
                    val loggedIn = currentState.userUiModel.userLoggedIn
                    if (loggedIn) {
                        navigator.replaceAll(CardListScreen())
                    } else {
                        navigator.replaceAll(UserEnteringScreen())
                    }
                }

                else -> {}
            }
        }
    }
}