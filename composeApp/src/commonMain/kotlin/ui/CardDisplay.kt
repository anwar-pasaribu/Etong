package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.CardUiModel


@Composable
fun CardContextMenu(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Hapus Kartu")

    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier.size(32.dp, 32.dp),
            onClick = { expanded = true },
            content = {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(items[index])
                        expanded = false
                    },
                    text = { Text(text = s, color = MaterialTheme.colorScheme.error) }
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreditCardItem(
    modifier: Modifier = Modifier,
    cardUiModel: CardUiModel,
    onCardClicked: (CardUiModel) -> Unit,
    onCardRemovalRequest: (CardUiModel) -> Unit,
) {
    var selectedCard by remember { mutableStateOf(false) }

    val elevation = if (selectedCard) {
        16.dp
    } else {
        0.dp
    }
    val animatedElevation = animateDpAsState(targetValue = elevation)

    val gradient =
        Brush.horizontalGradient(listOf(Color(0xFF28D8A3), Color(0xFF00BEB2)))

    Card(
        modifier = modifier.then(Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 160.dp)),
        elevation = CardDefaults.cardElevation(animatedElevation.value),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 0.dp, color = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.background(gradient).combinedClickable(
                onClick = {
                    selectedCard = !selectedCard
                    onCardClicked(cardUiModel)
                }
            ).padding(all = 8.dp).fillMaxHeight()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("**** - ${cardUiModel.cardNumber}")

                Row(
                    modifier = Modifier.fillMaxWidth().requiredHeight(32.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    AnimatedVisibility(selectedCard) {
                        CardContextMenu {
                            onCardRemovalRequest(cardUiModel)
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                CurrencyAmountDisplay(
                    modifier = Modifier.weight(1F),
                    amount = cardUiModel.billMinAmount
                )
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    CurrencyAmountDisplay(
                        amount = cardUiModel.billAmount
                    )
                }
            }
            DueDateView(cardUiModel)
        }
    }
}