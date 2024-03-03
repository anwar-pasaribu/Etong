package model

import org.jetbrains.compose.resources.DrawableResource
import utils.cardutils.CardType

data class CardUiModel(
    val cardId: String,
    val cardLabel: String,
    val cardType: CardType,
    val cardLogo: CardLogo,
    val billAmount: Double,
    val billMinAmount: Double,
    val billingDate: Long,
    val billDueDate: Long
)