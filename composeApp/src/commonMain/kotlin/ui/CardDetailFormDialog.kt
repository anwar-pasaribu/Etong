package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.Clock
import model.CardUiModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputCardDetail(onDismissRequest: () -> Unit, onSubmitRequest: (CardUiModel) -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
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
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = cardNumber, onValueChange = {
                        cardNumber = it
                    },
                    placeholder = {
                        Text(text = "Nomor Kartu")
                    },
                    label = {
                        Text(text = "CC")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = billAmount, onValueChange = {
                        billAmount = it
                    },
                    placeholder = {
                        Text(text = "Nominal Tagihan Full")
                    },
                    label = { Text(text = "Total Tagihan") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next)
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = billMinAmount, onValueChange = {
                        billMinAmount = it
                    },
                    placeholder = {
                        Text(text = "Minimum Tagihan")
                    },
                    label = { Text(text = "Minimum Tagihan") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val cardDetail = CardUiModel(
                        "",
                        cardNumber = cardNumber,
                        billAmount = billAmount.toDouble(),
                        billMinAmount = billMinAmount.toDouble(),
                        billDueDate = 0L,
                        billingDate = 0L
                    )
                    onSubmitRequest(cardDetail)
                    onDismissRequest()
                }) {
                    Text(text = "Tambah")
                }
            }
        }

    }
}