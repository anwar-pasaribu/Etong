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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.EtongAppDI
import model.CardUiModel
import ui.CardContextMenu
import ui.CreditCardItem
import ui.InputPaidAmountDetail
import ui.LoadingView
import ui.PaidAmountView
import utils.toDdMonth
import viewmodel.CardDetailScreenModel

data class CardDetailScreen(val cardUiModel: CardUiModel) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val cardDetailScreenModel = rememberScreenModel { EtongAppDI.cardDetailScreenModel }
        cardDetailScreenModel.observeCardPayment(cardUiModel)
        val state = remember { cardDetailScreenModel.state }
        val navigator = LocalNavigator.currentOrThrow
        val openAddPaidAmountDialog = remember { mutableStateOf(false) }
        val paidAmount = remember { mutableStateOf(0.0) }
        Surface {
            Column(
                modifier =
                Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars
                    )
                )

                Row(
                    modifier = Modifier.requiredHeight(56.dp).padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1F).fillMaxWidth().requiredHeight(32.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            modifier = Modifier.size(32.dp, 32.dp),
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null,
                                )
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1F).fillMaxWidth().requiredHeight(32.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CardContextMenu {
                            cardDetailScreenModel.removeCard(cardUiModel)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                CreditCardItem(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    cardUiModel = cardUiModel,
                    onCardClicked = {},
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        openAddPaidAmountDialog.value = true
                    },
                ) {
                    Text(
                        text = "Sudah bayar?",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                PaidAmountView(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    amount = cardUiModel.billAmount,
                    paid = paidAmount.value
                )

                Spacer(Modifier.height(16.dp))

                when (val currentState = state.value) {
                    CardDetailScreenModel.CardDetailScreenState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingView()
                        }
                    }

                    is CardDetailScreenModel.CardDetailScreenState.Success -> {
                        paidAmount.value = currentState.paidAmount
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                horizontal = 10.dp,
                                vertical = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(
                                items = currentState.cardPaymentList
                            ) { cardPayment ->
                                Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    Text(
                                        text = cardPayment.paymentDate.toDdMonth(),
                                        modifier = Modifier.wrapContentWidth()
                                            .align(Alignment.CenterStart),
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                    Text(
                                        text = cardPayment.amount.formatNominal,
                                        modifier = Modifier.wrapContentWidth()
                                            .align(Alignment.CenterEnd),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    is CardDetailScreenModel.CardDetailScreenState.DeleteSuccess -> {
                        navigator.pop()
                    }

                    is CardDetailScreenModel.CardDetailScreenState.Failure -> {
                        androidx.compose.animation.AnimatedVisibility(true) {
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

            when {
                openAddPaidAmountDialog.value -> {
                    InputPaidAmountDetail(
                        billAmount = cardUiModel.billAmount,
                        onDismissRequest = {
                            openAddPaidAmountDialog.value = false
                        },
                        onSubmitRequest = { cardPayment ->
                            cardDetailScreenModel.addNewCardPayment(
                                cardPayment.copy(
                                    cardId = cardUiModel.cardId
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
