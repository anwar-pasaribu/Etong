package ui.screen

import CardDetailScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.btn_logout
import etong.composeapp.generated.resources.content_desc_add_card
import etong.composeapp.generated.resources.label_empty_no_card
import etong.composeapp.generated.resources.label_home
import etong.composeapp.generated.resources.msg_confirm_logout
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import ui.ConfirmDialog
import ui.CreditCardItem
import ui.InputCardDetail
import ui.LoadingView
import ui.TotalBillCardItem
import viewmodel.CardScreenModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
class CardListScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(ExperimentalResourceApi::class, ExperimentalHazeMaterialsApi::class)
    @Composable
    override fun Content() {
        val cardScreenModel = koinInject<CardScreenModel>()
        val state = remember { cardScreenModel.state }
        val openAddCardDialog = remember { mutableStateOf(false) }
        val fabVisible = remember { mutableStateOf(false) }
        val confirmLogoutDialogVisible = remember { mutableStateOf(false) }
        val navigator = LocalNavigator.currentOrThrow
        val hazeState = remember { HazeState() }

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .hazeChild(
                            state = hazeState,
                            style = HazeMaterials.thin(MaterialTheme.colorScheme.background)
                        ),
                    colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
                    title = { Text(stringResource(Res.string.label_home)) },
                    actions = {
                        IconButton(
                            modifier = Modifier.size(32.dp, 32.dp),
                            onClick = {
                                navigator.push(ChatScreen())
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Chat,
                                    contentDescription = null,
                                )
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            modifier = Modifier.size(32.dp, 32.dp),
                            onClick = {
                                confirmLogoutDialogVisible.value = true
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = null,
                                )
                            }
                        )
                    }
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = fabVisible.value,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    FloatingActionButton(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                            .size(64.dp),
                        shape = CircleShape,
                        onClick = { openAddCardDialog.value = true },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(Res.string.content_desc_add_card),
                            )
                        }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { contentPadding ->
            when (val currentState = state.value) {
                is CardScreenModel.MainScreenState.Loading -> {
                    AnimatedVisibility(true) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingView()
                        }
                    }
                }

                is CardScreenModel.MainScreenState.Success -> {
                    fabVisible.value = true
                    AnimatedVisibility(visible = true) {
                        CardListView(
                            contentPadding,
                            currentState,
                            navigator,
                            hazeState
                        )
                    }
                }

                is CardScreenModel.MainScreenState.Empty -> {
                    fabVisible.value = true
                    AnimatedVisibility(true) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(Res.string.label_empty_no_card),
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                is CardScreenModel.MainScreenState.Failure -> {
                    AnimatedVisibility(true) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Failure - ${currentState.errorMessage}",
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                else -> {}
            }
        }

        when {
            openAddCardDialog.value -> {
                InputCardDetail(
                    onDismissRequest = {
                        openAddCardDialog.value = false
                    },
                    onSubmitRequest = { newCard ->
                        cardScreenModel.addNewCard(newCard)
                    }
                )
            }

            confirmLogoutDialogVisible.value -> {
                ConfirmDialog(
                    message = stringResource(Res.string.msg_confirm_logout),
                    positiveActionText = stringResource(Res.string.btn_logout),
                    onPositiveAction = {
                        navigator.replaceAll(UserEnteringScreen(logoutRequested = true))
                    },
                    onDismissRequest = {
                        confirmLogoutDialogVisible.value = false
                    }
                )
            }
        }
    }

    @Composable
    private fun CardListView(
        contentPadding: PaddingValues,
        currentState: CardScreenModel.MainScreenState.Success,
        navigator: Navigator,
        hazeState: HazeState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("lazy_grid")
                .haze(state = hazeState),
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + 8.dp,
                top = contentPadding.calculateTopPadding() + 16.dp,
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr) + 8.dp,
                bottom = contentPadding.calculateBottomPadding()

            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(
                items = currentState.cardList,
                key = { it.cardId }
            ) { cardItem ->
                CreditCardItem(
                    modifier = Modifier.animateItemPlacement(),
                    cardUiModel = cardItem,
                    onCardClicked = {
                        navigator.push(CardDetailScreen(it))
                    }
                )
            }
            item {
                TotalBillCardItem(
                    cardUiModel = currentState.totalBillCard,
                )
            }
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
            }
        }
    }
}