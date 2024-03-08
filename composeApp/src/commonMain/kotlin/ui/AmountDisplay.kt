package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.label_idr
import formatNominal
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CurrencyAmountDisplay(modifier: Modifier = Modifier, amount: Double = 0.0) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringResource(Res.string.label_idr),
            modifier = Modifier.alignByBaseline(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = amount.formatNominal,
            modifier = Modifier.alignByBaseline(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
