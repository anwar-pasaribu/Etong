package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.btn_delete_card
import model.CardUiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import ui.component.ImageWrapper
import ui.extension.bouncingClickable


@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardContextMenu(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    val items = listOf(stringResource(Res.string.btn_delete_card))

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

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CreditCardItem(
    modifier: Modifier = Modifier,
    cardUiModel: CardUiModel,
    onCardClicked: (CardUiModel) -> Unit,
) {

    val darkGradient = remember {
        Brush.horizontalGradient(listOf(Color(0xFF004440), Color(0xFF00312e)))
    }
    val lightGradient = remember {
        Brush.horizontalGradient(listOf(Color(0xFF28D8A3), Color(0xFF00BEB2)))
    }

    val gradient = if (isSystemInDarkTheme()) {
        darkGradient
    } else {
        lightGradient
    }

    Card(
        modifier = modifier.then(
            Modifier.fillMaxWidth()
                .heightIn(min = 120.dp, max = 160.dp)
                .bouncingClickable(
                    onClick = { onCardClicked(cardUiModel)},
                )
        ),
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = MaterialTheme.shapes.medium,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.background(gradient).padding(all = 8.dp).fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.height(32.dp).fillMaxWidth().align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImageWrapper(
                        modifier = Modifier.height(24.dp),
                        alignment = Alignment.CenterStart,
                        resource = cardUiModel.cardLogo.resource,
                        contentScale = ContentScale.FillHeight,
                        contentDescription = cardUiModel.cardType.name
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = cardUiModel.cardLabel)
                }
                Row(
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                        .align(Alignment.CenterStart),
                ) {
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
                DueDateView(
                    modifier = Modifier.align(Alignment.BottomStart),
                    billDueDate = cardUiModel.billDueDate
                )
            }
        }
    }
}

@Composable
fun TotalBillCardItem(
    modifier: Modifier = Modifier,
    cardUiModel: CardUiModel,
) {

    Card(
        modifier = modifier.then(Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 256.dp)),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(width = 0.dp, color = Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(bottom = 56.dp).fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.height(32.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.height(24.dp),
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Total minimum payment"
                )
            }

            CurrencyAmountDisplay(
                modifier = Modifier.testTag("test-min-amount"),
                amount = cardUiModel.billMinAmount
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.height(32.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.height(24.dp),
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Total full payment"
                )
            }

            CurrencyAmountDisplay(
                modifier = Modifier.testTag("test-full-amount"),
                amount = cardUiModel.billAmount
            )
        }
    }
}