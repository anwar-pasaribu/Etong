package model

data class CardUiModel(
    val cardId: String,
    val cardNumber: String,
    val billAmount: Double,
    val billMinAmount: Double,
    val billingDate: Long,
    val billDueDate: Long
)