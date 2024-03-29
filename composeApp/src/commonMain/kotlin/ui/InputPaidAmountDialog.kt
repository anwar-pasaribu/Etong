package ui

import ui.component.InputNumber
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import model.CardPaymentUiModel
import ui.component.BackButton
import kotlin.math.roundToLong

@Composable
fun InputPaidAmountDetail(
    billAmount: Double = 0.0,
    onDismissRequest: () -> Unit,
    onSubmitRequest: (cardPaymentUiModel: CardPaymentUiModel) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = false),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

                    BackButton {
                        onDismissRequest()
                    }

                    Spacer(Modifier.height(16.dp))

                    var paidAmount by remember { mutableStateOf("") }
                    Button(
                        shape = MaterialTheme.shapes.small,
                        onClick = {
                            paidAmount = billAmount.roundToLong().toString()
                        }
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            text = "Pembayaran penuh"
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Input nominal",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start
                    )
                    InputNumber(
                        initialValue = paidAmount,
                        label = "Pembayaran",
                        placeholder = "Total Pembayaran",
                        onTyped = {
                            paidAmount = it
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tanggal Pembayaran",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    var paidDateMillis by remember { mutableStateOf(0L) }
                    InputDateManual() {
                        val selectedDueDate = it.toLocalDateTime()
                        paidDateMillis = selectedDueDate.toInstant(TimeZone.currentSystemDefault())
                            .toEpochMilliseconds()
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val buttonEnabled = paidAmount.isNotEmpty()
                            && paidDateMillis != 0L
                    Button(onClick = {
                        onSubmitRequest(
                            CardPaymentUiModel(
                                id = "",
                                cardId = "",
                                amount = paidAmount.toDouble(),
                                paymentDate = paidDateMillis,
                                note = "Note: Pay $paidAmount "
                            )
                        )
                        onDismissRequest()
                    }, enabled = buttonEnabled) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            text = "Tambah"
                        )
                    }
                }
            }
        }
    }
}