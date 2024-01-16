package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputPaidAmountDetail(
    onDismissRequest: () -> Unit,
    onSubmitRequest: (paidAmount: Double, paymentDate: Long) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = true),
    ) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var paidAmount by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = paidAmount, onValueChange = {
                        paidAmount = it
                    },
                    placeholder = {
                        Text(text = "Total Pembayaran")
                    },
                    label = {
                        Text(text = "Pembayaran")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
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
                    onSubmitRequest(paidAmount.toDouble(), paidDateMillis)
                    onDismissRequest()
                }, enabled = buttonEnabled ) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        text = "Tambah"
                    )
                }
            }
        }
    }
}