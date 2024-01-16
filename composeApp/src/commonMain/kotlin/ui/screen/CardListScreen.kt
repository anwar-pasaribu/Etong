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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.EtongAppDI
import ui.CreditCardItem
import ui.InputCardDetail
import ui.LoadingView
import viewmodel.CardScreenModel

@OptIn(ExperimentalFoundationApi::class)
class CardListScreen : Screen {

    @Composable
    override fun Content() {
        val cardScreenModel = rememberScreenModel { EtongAppDI.cardScreenModel }
        val state = remember { cardScreenModel.state }
        val openAddCardDialog = remember { mutableStateOf(false) }
        val fabVisible = remember { mutableStateOf(false) }
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
                        onClick = { openAddCardDialog.value = true },
                        shape = CircleShape,
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add new card",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
        ) { innerPadding ->
            Surface {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (val currentState = state.value) {
                        CardScreenModel.MainScreenState.Loading -> {
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
                            AnimatedVisibility(
                                visible = true,
                                enter = scaleIn(initialScale = .9F),
                                exit = scaleOut(),
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
                                        Column {
                                            Spacer(
                                                Modifier.windowInsetsTopHeight(
                                                    WindowInsets.statusBars
                                                )
                                            )
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(Color.White)
                                            ) {
                                                Text(
                                                    style = MaterialTheme.typography.displayLarge,
                                                    text = "Total bill"
                                                )
                                            }
                                        }
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
                                            },
                                            onCardRemovalRequest = {
                                                cardScreenModel.removeCard(it)
                                            }
                                        )
                                    }
                                    item {
                                        Spacer(
                                            Modifier.windowInsetsBottomHeight(
                                                WindowInsets.systemBars
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        CardScreenModel.MainScreenState.Empty -> {
                            AnimatedVisibility(true) {
                                Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = "Tidak ada Kartu",
                                        modifier = Modifier.fillMaxSize().padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        is CardScreenModel.MainScreenState.Failure -> {
                            AnimatedVisibility(true) {
                                Text(
                                    text = "Failure - ${currentState.errorMessage}",
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
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
        }
    }
}