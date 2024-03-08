package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.american_express
import etong.composeapp.generated.resources.baseline_credit_card_24
import etong.composeapp.generated.resources.dinners_club
import etong.composeapp.generated.resources.discover
import etong.composeapp.generated.resources.jcb
import etong.composeapp.generated.resources.maestro
import etong.composeapp.generated.resources.mastercard
import etong.composeapp.generated.resources.visa
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.component.ImageWrapper
import ui.extension.bouncingClickable
import utils.cardutils.CardType

@OptIn(ExperimentalAnimationApi::class, ExperimentalResourceApi::class)
@Composable
fun DropdownCardTypeSection(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit
) {
    val cardsV2 = listOf(
        Triple(CardType.UNKNOWN, "Pilih Jenis Kartu", Res.drawable.baseline_credit_card_24),
        Triple(CardType.VISA, "Visa", Res.drawable.visa),
        Triple(CardType.MASTERCARD, "Mastercard", Res.drawable.mastercard),
        Triple(CardType.JCB, "JCB", Res.drawable.jcb),
        Triple(CardType.AMERICAN_EXPRESS, "American Express", Res.drawable.american_express),
        Triple(CardType.DISCOVER, "Discover", Res.drawable.discover),
        Triple(CardType.DINNERS_CLUB, "Dinners Club", Res.drawable.dinners_club),
        Triple(CardType.MAESTRO, "Maestro", Res.drawable.maestro),
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(1) }

    Box(
        modifier = modifier.then(
            Modifier
                .bouncingClickable {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    expanded = true
                }
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.shapes.small
                )
                .padding(horizontal = 6.dp, vertical = 6.dp)
        ),
        contentAlignment = Alignment.Center
    ) {
        val updatedItem = cardsV2[selectedIndex]
        val icon = updatedItem.third
        AnimatedContent(
            targetState = icon,
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            }
        ) { updatedValue ->
            ImageWrapper(
                modifier = Modifier.size(width = 32.dp, height = 24.dp).animateEnterExit(
                    enter = fadeIn(),
                    exit = fadeOut()
                ),
                alignment = Alignment.CenterStart,
                resource = updatedValue,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cardsV2.forEachIndexed { index, item ->
                val itemId = item.first
                val itemLabel = item.second
                val itemIconSrc = item.third
                val itemSelected = index != 0 && selectedIndex == index
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(itemId.name)
                        selectedIndex = index
                        expanded = false
                    },
                    leadingIcon = {
                        if (index != 0) {
                            ImageWrapper(
                                modifier = Modifier.size(width = 32.dp, height = 24.dp),
                                alignment = Alignment.Center,
                                resource = itemIconSrc,
                                contentScale = ContentScale.Fit,
                                contentDescription = itemLabel
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(width = 32.dp, height = 24.dp),
                                imageVector = Icons.Filled.CreditCard,
                                contentDescription = ""
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(),
                    enabled = index != 0,
                    text = {
                        val fontStyle = if (index == 0) {
                            MaterialTheme.typography.bodyMedium
                        } else {
                            MaterialTheme.typography.bodyLarge
                        }
                        val fontWeight = if (itemSelected) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        }
                        Text(
                            text = itemLabel,
                            style = fontStyle,
                            fontWeight = fontWeight
                        )
                    }
                )
            }
        }
    }
}