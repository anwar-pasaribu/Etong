package com.unwur.etong

//import CreditCardItem
import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}

@Preview
@Composable
fun CardItemPreview() {
    LazyColumn {
        items(3) {
            CreditCardItemTest()
        }
    }
}

@Preview
@Composable
fun InputCardDetailPreview() {
    InputCardDetail({})
}

@Composable
fun InputCardDetail(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "1234", onValueChange = {

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
                    value = "1.000.000", onValueChange = {

                    },
                    placeholder = {
                        Text(text = "Nominal Tagihan Full")
                    },
                    label = {
                        Text(text = "Total Tagihan")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "500.000", onValueChange = {

                    },
                    placeholder = {
                        Text(text = "Minimum Tagihan")
                    },
                    label = {
                        Text(text = "Minimum Tagihan")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Tambah")
                }
            }
        }

    }
}

@Composable
fun CreditCardItemTest() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        elevation = 16.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(all = 8.dp)) {
            Row {
                Text("bank_ic")
                Spacer(modifier = Modifier.width(16.dp))
                Text("**** - 7890")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .alignByBaseline(),
                    text = "Rp 150.0000", fontSize = 24.sp,
                    color = Color.Blue
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(align = Alignment.End)
                        .alignByBaseline(),
                    text = "Rp 430.000",
                    fontSize = 32.sp
                )
            }

            Row {
                Text("15 Oct")
                Spacer(modifier = Modifier.width(16.dp))
                Text("2 Nov")
            }
        }
    }
}