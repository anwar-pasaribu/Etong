package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import model.CardLogo
import model.CardUiModel
import ui.component.BackButton
import ui.component.InputNumber
import utils.cardutils.CardType

@Composable
fun InputCardDetail(
    onDismissRequest: () -> Unit,
    onSubmitRequest: (CardUiModel) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = false),
    ) {
        Column(
            modifier = Modifier.requiredWidth(320.dp),
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

                    var cardLabel by remember { mutableStateOf("") }
                    var cardTypeString by remember { mutableStateOf(CardType.VISA.name) }
                    var billAmount by remember { mutableStateOf("") }
                    var billMinAmount by remember { mutableStateOf("") }

                    Row (verticalAlignment = Alignment.CenterVertically) {
                        DropdownCardTypeSection(
                            modifier = Modifier.wrapContentWidth().defaultMinSize(minWidth = 64.dp).padding(end = 8.dp),
                            onItemSelected = {
                                cardTypeString = it
                            }
                        )
                        OutlinedTextField(
                            modifier = Modifier.weight(.9f),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            value = cardLabel,
                            onValueChange = {
                                if (it.length <= 16) cardLabel = it
                            },
                            singleLine = true,
                            placeholder = { Text(text = "cth. Visa BCA") },
                            label = { Text(text = "Nama Kartu") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InputNumber(
                        label = "Total Tagihan",
                        placeholder = "Total Tagihan",
                        onTyped = {
                            billAmount = it
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    InputNumber(
                        label = "Minimum Tagihan",
                        placeholder = "Minimum Tagihan",
                        onTyped = {
                            billMinAmount = it
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Tanggal Jatuh Tempo",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    var dueDateMillis by remember { mutableStateOf(0L) }
                    InputDateManual() {
                        val selectedDueDate = it.toLocalDateTime()
                        dueDateMillis = selectedDueDate.toInstant(TimeZone.currentSystemDefault())
                            .toEpochMilliseconds()
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    val buttonEnabled = cardLabel.isNotEmpty()
                            && billAmount.isNotEmpty()
                            && billMinAmount.isNotEmpty()
                            && cardTypeString.isNotEmpty()
                            && dueDateMillis != 0L
                    Button(onClick = {
                        val cardDetail = CardUiModel(
                            "",
                            cardLabel = cardLabel,
                            billAmount = billAmount.toDouble(),
                            billMinAmount = billMinAmount.toDouble(),
                            billDueDate = dueDateMillis,
                            billingDate = 0L,
                            cardType = CardType.valueOf(cardTypeString),
                            cardLogo = CardLogo.genericLogo()
                        )
                        onSubmitRequest(cardDetail)
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
