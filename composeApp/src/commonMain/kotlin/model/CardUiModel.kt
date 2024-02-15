package model

import utils.cardutils.CardType

data class CardUiModel(
    val cardId: String,
    val cardNumber: String,
    var cardType: CardType = CardType.UNKNOWN,
    var cardLogo: String = "",
    val billAmount: Double,
    val billMinAmount: Double,
    val billingDate: Long,
    val billDueDate: Long
)