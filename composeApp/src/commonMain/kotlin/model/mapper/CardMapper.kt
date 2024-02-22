package model.mapper

import io.realm.kotlin.types.RealmUUID
import model.CardPaymentUiModel
import model.CardUiModel
import model.storage.Card
import model.storage.CardPayment
import utils.cardutils.CardType
import utils.cardutils.identifyCardTypeFromCardTypeEnumName
import utils.cardutils.identifyCardTypeFromNumber

fun CardUiModel.toDbInstance() : Card {

    val card = Card()
    if (cardId.isNotEmpty()) {
        card._id = RealmUUID.Companion.from(cardId)
    }
    card.cardNumber = "-"
    card.cardLabel = cardLabel
    card.cardType = cardType.name
    card.billAmount = billAmount
    card.billMinAmount = billMinAmount
    card.billingDate = billingDate
    card.billDueDate = billDueDate

    return card
}


fun Card.toUiModel() : CardUiModel {
    val cardType = identifyCardTypeFromCardTypeEnumName(this.cardType)
    val cardLogo = when(cardType) {
        CardType.VISA -> { "visa.png" }
        CardType.JCB -> { "jcb.png" }
        CardType.MASTERCARD -> { "mastercard.png" }
        CardType.MAESTRO -> { "maestro.png" }
        CardType.AMERICAN_EXPRESS -> { "american_express.png" }
        CardType.DINNERS_CLUB -> { "dinners_club.png" }
        CardType.DISCOVER -> { "discover.png" }
        else -> { "" }
    }

    return CardUiModel(
        cardId = this._id.toString(),
        cardLabel = this.cardLabel,
        cardType = cardType,
        cardLogo = cardLogo,
        billAmount = this.billAmount,
        billMinAmount = this.billMinAmount,
        billingDate = this.billingDate,
        billDueDate = this.billDueDate,
    )
}

fun CardPaymentUiModel.toDbInstance() : CardPayment {

    val card = CardPayment()
    if (id.isNotEmpty()) {
        card._id = RealmUUID.Companion.from(this.id)
    }
    card.cardId = this.cardId
    card.amount = this.amount
    card.paymentDate = this.paymentDate
    card.note = this.note

    return card
}
fun CardPayment.toUiModel() : CardPaymentUiModel {
    return CardPaymentUiModel(
        id = this._id.toString(),
        cardId = this.cardId,
        amount = this.amount,
        paymentDate = this.paymentDate,
        note = this.note,
    )
}
