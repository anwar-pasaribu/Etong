package ui.screen

import CardDetailScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.EtongAppDI
import ui.ConfirmDialog
import ui.CreditCardItem
import ui.InputCardDetail
import ui.LoadingView
import ui.TotalBillCardItem
import viewmodel.CardScreenModel

@OptIn(ExperimentalFoundationApi::class)
class CardListScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val cardScreenModel = rememberScreenModel { EtongAppDI.cardScreenModel }
        val state = remember { cardScreenModel.state }
        val openAddCardDialog = remember { mutableStateOf(false) }
        val fabVisible = remember { mutableStateOf(false) }
        val confirmLogoutDialogVisible = remember { mutableStateOf(false) }
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(
                    visible = fabVisible.value,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    FloatingActionButton(
                        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                        shape = FloatingActionButtonDefaults.largeShape,
                        onClick = { openAddCardDialog.value = true },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new card",
                            )
                        }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { _ ->
            Surface {
                Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                    when (val currentState = state.value) {
                        is CardScreenModel.MainScreenState.Loading -> {
                            AnimatedVisibility(true) {
                                Column (
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
                                    confirmLogoutDialogVisible,
                                    currentState,
                                    navigator
                                )
                            }
                        }
                        is CardScreenModel.MainScreenState.Empty -> {
                            fabVisible.value = true
                            AnimatedVisibility(true) {
                                Column (
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Tidak ada Kartu, tekan tombol + untuk menambah.",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        is CardScreenModel.MainScreenState.Failure -> {
                            AnimatedVisibility(true) {
                                Column (
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
                    message = "Kamu mau logout dari aplikasi?",
                    positiveActionText = "Logout",
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
        confirmLogoutDialogVisible: MutableState<Boolean>,
        currentState: CardScreenModel.MainScreenState.Success,
        navigator: Navigator
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars
                    )
                )
            }
            stickyHeader {
                CardListHeader(confirmLogoutDialogVisible)
            }
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

    @Composable
    fun CardListHeader(confirmLogoutDialogVisible: MutableState<Boolean>) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.displayMedium,
                    text = "Bills"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
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
            }
        }
    }
}