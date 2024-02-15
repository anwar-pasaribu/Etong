package model

data class CardPaymentUiModel(
    var id: String,
    var cardId: String,
    var amount: Double,
    var paymentDate: Long,
    var note: String
)