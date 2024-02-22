package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import utils.cardutils.CardType

@OptIn(ExperimentalAnimationApi::class, ExperimentalResourceApi::class)
@Composable
fun DropdownCardTypeSection(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit
) {
    val cards = listOf(
        Triple(CardType.UNKNOWN, "Pilih Jenis Kartu", ""),
        Triple(CardType.VISA, "Visa", "visa.png"),
        Triple(CardType.MASTERCARD, "Mastercard", "mastercard.png"),
        Triple(CardType.JCB, "JCB", "jcb.png"),
        Triple(CardType.AMERICAN_EXPRESS, "American Express", "american_express.png"),
        Triple(CardType.DISCOVER, "Discover", "discover.png"),
        Triple(CardType.DINNERS_CLUB, "Dinners Club", "dinners_club.png"),
        Triple(CardType.MAESTRO, "Maestro", "maestro.png"),
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(1) }

    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.shapes.small
                )
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        expanded = true
                    }
                )
                .padding(horizontal = 6.dp, vertical = 6.dp)
        )
    ) {
        val updatedItem = cards[selectedIndex]
        val icon = updatedItem.third
        val painterState = painterResource(DrawableResource(icon))
        AnimatedContent(
            targetState = painterState,
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            }
        ) { updatedValue ->
            Image(
                modifier = Modifier.size(width = 32.dp, height = 24.dp).animateEnterExit(
                    enter = fadeIn(),
                    exit = fadeOut()
                ),
                alignment = Alignment.CenterStart,
                painter = updatedValue,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cards.forEachIndexed { index, item ->
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
                            Image(
                                modifier = Modifier.size(width = 32.dp, height = 24.dp),
                                alignment = Alignment.Center,
                                painter = painterResource(DrawableResource(itemIconSrc)),
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