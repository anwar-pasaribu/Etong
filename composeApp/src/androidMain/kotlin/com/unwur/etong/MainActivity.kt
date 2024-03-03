package com.unwur.etong

import App
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.initialize
import ui.InputCardDetail
import ui.InputPaidAmountDetail
import ui.PaidAmountView
import ui.UserEnteringScreenModeToggle
import ui.theme.EtongTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.initialize(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = getColor(R.color.etongPrimary),
                darkScrim = getColor(R.color.etongPrimaryDark)
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppAndroidPreview() {
    EtongTheme {
        val testAmount = listOf(
            Pair(150_000_000.0, 230_000_000.0),
            Pair(15_000_000.0, 230_000_000.0),
            Pair(230_000_000.0, 230_000_000.0),
            Pair(50_000_000.0, 230_000_000.0),
        )
        LazyColumn {
            items(testAmount) {
                PaidAmountView(paid = it.first, amount = it.second)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun BarChart(values: List<Int>) {
    val maxValue = values.maxOrNull() ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        values.forEachIndexed { _, value ->
            Bar(
                value = value,
                maxValue = maxValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun Bar(value: Int, maxValue: Int, modifier: Modifier = Modifier) {
    val percentage = (value.toFloat() / maxValue.toFloat()) * 100
    val color = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.2f))
            .padding(4.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth(percentage / 100f)
                .fillMaxHeight()
                .background(
                    brush = Brush.verticalGradient(listOf(Color.Blue, Color.Green)),
                    shape = MaterialTheme.shapes.small
                )
        ) {}
        Text(
            text = value.toString(),
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingViewPreview() {
    EtongTheme {
        LoadingView {

        }
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun LoadingView(modifier: Modifier = Modifier, skeletonLayout: @Composable () -> Unit = {}) {
    Box(
        modifier = modifier.then(
            Modifier
                .width(24.dp)
                .height(24.dp)
        )
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedButtonPreview() {
    EtongTheme {
        UserEnteringScreenModeToggle(
            modifier = Modifier.padding(top = 32.dp),
            onRegisterMode = {}
        ) {}
    }
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun InputPaidAmountDetailPreview() {
    EtongTheme {
        InputPaidAmountDetail(1000_000_000_000_000.0, {}, {})
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun InputCardDetailPreview() {
    EtongTheme {
        InputCardDetail({}, {})
    }
}