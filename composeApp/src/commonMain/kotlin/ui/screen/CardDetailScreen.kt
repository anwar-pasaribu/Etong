import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.EtongAppDI
import model.CardUiModel
import ui.CardContextMenu
import ui.CreditCardItem
import ui.InputPaidAmountDetail
import ui.PaidAmountView

data class CardDetailScreen(val cardUiModel: CardUiModel) : Screen {

    @Composable
    override fun Content() {
        val cardScreenModel = rememberScreenModel { EtongAppDI.cardScreenModel }
        val navigator = LocalNavigator.currentOrThrow
        val openAddPaidAmountDialog = remember { mutableStateOf(false) }
        val paidAmount = remember { mutableStateOf(0.0) }
        Surface {
            Column(
                modifier =
                Modifier.fillMaxSize()
                    .background(Color.Red)
                    .padding(start = 4.dp, top = 16.dp, end = 4.dp)
            ) {
                Spacer(
                    Modifier.windowInsetsTopHeight(
                        WindowInsets.statusBars
                    )
                )

                Row {
                    Row(
                        modifier = Modifier.weight(1F).fillMaxWidth().requiredHeight(32.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        IconButton(
                            modifier = Modifier.size(32.dp, 32.dp),
                            onClick = { navigator.pop() },
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1F).fillMaxWidth().requiredHeight(32.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CardContextMenu {
                            cardScreenModel.removeCard(cardUiModel)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                CreditCardItem(cardUiModel = cardUiModel,
                    onCardClicked = {},
                    onCardRemovalRequest = {}
                )

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
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
                    amount = cardUiModel.billAmount,
                    paid = paidAmount.value
                )
            }

            when {
                openAddPaidAmountDialog.value -> {
                    InputPaidAmountDetail(
                        onDismissRequest = {
                            openAddPaidAmountDialog.value = false
                        },
                        onSubmitRequest = { paid, paymentDate ->
                            paidAmount.value = paid
                        }
                    )
                }
            }
        }
    }
}
