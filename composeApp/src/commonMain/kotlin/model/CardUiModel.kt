package model

import utils.cardutils.CardType

data class CardUiModel(
    val cardId: String,
    val cardLabel: String,
    val cardType: CardType,
    val cardLogo: String,
    val billAmount: Double,
    val billMinAmount: Double,
    val billingDate: Long,
    val billDueDate: Long
)