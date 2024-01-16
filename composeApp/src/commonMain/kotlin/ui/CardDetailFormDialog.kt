package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import model.CardUiModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputCardDetail(onDismissRequest: () -> Unit, onSubmitRequest: (CardUiModel) -> Unit) {
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
                var cardNumber by remember { mutableStateOf("") }
                var billAmount by remember { mutableStateOf("") }
                var billMinAmount by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = cardNumber, onValueChange = {
                        cardNumber = it
                    },
                    placeholder = {
                        Text(text = "Nomor Kartu")
                    },
                    label = {
                        Text(text = "Kartu")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = billAmount,
                    onValueChange = {
                        billAmount = it
                    },
                    placeholder = { Text(text = "Total Tagihan") },
                    label = { Text(text = "Total Tagihan") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = billMinAmount, onValueChange = {
                        billMinAmount = it
                    },
                    placeholder = {
                        Text(text = "Minimum Tagihan")
                    },
                    label = { Text(text = "Minimum Tagihan") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
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
                val buttonEnabled = cardNumber.isNotEmpty()
                        && billAmount.isNotEmpty()
                        && billMinAmount.isNotEmpty()
                        && dueDateMillis != 0L
                Button(onClick = {
                    val cardDetail = CardUiModel(
                        "",
                        cardNumber = cardNumber,
                        billAmount = billAmount.toDouble(),
                        billMinAmount = billMinAmount.toDouble(),
                        billDueDate = dueDateMillis,
                        billingDate = 0L
                    )
                    onSubmitRequest(cardDetail)
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

@Composable
fun InputDateManual(modifier: Modifier = Modifier, onInputFinished: (String) -> Unit) {

    var dayDd by remember { mutableStateOf("") }
    var monthMm by remember { mutableStateOf("") }
    var yearYy by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        val dayItems = mutableListOf<String>()
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        for (dayIndex in 0..31) {
            if (dayIndex == 0) {
                dayItems.add("Tgl")
            } else {
                dayItems.add(dayIndex.toString())
            }
        }
        val monthList = listOf("Bulan", "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des")

        DropdownSection(
            modifier = Modifier.weight(1F).fillMaxWidth(),
            items = dayItems
        ) {
            dayDd = it.padStart(2, '0')
        }

        DropdownSection(
            modifier = Modifier.weight(1F),
            items = monthList
        ) {
            monthMm = monthList.indexOf(it).toString().padStart(2, '0')
        }

        val yearString = today.year.toString()
        DropdownSection(
            modifier = Modifier.weight(1F),
            items = listOf("Tahun", yearString)
        ) {
            yearYy = it
        }

        if (dayDd.isNotEmpty() && monthMm.isNotEmpty() && yearYy.isNotEmpty()) {
            onInputFinished("$yearString-$monthMm-${dayDd}T00:00:00")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownSection(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier.clickable(
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    expanded = true
                }
            ).background(
                Color.LightGray,
                MaterialTheme.shapes.small
            ).padding(horizontal = 16.dp, vertical = 6.dp).fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(items[index])
                        selectedIndex = index
                        expanded = false
                    },
                    enabled = index != 0,
                    text = {
                        Text(text = s)
                    }
                )
            }
        }
    }
}