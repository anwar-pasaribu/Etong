package preview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.PaidAmountView
import ui.theme.EtongTheme

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