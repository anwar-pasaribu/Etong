package ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.CrossfadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import config.PlatformType
import di.EtongAppDI
import getPlatform
import viewmodel.HomeScreenModel

class HomeScreen: Screen {

    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { EtongAppDI.homeScreenModel }
        val state = remember { screenModel.state }

        when (val currentState = state.value) {
            is HomeScreenModel.HomeScreenState.Success -> {
                val loggedIn = currentState.userUiModel.userLoggedIn
                if (loggedIn) {
                    Navigator(CardListScreen()) { appNavigator ->
                        val supportSwipeBack = remember { getPlatform().type == PlatformType.IOS }
                        if (supportSwipeBack) {
                            SlideTransition(navigator = appNavigator)
                        } else {
                            CrossfadeTransition(navigator = appNavigator)
                        }
                    }
                } else {
                    Navigator(UserEnteringScreen())
                }
            }

            else -> {}
        }
    }
}