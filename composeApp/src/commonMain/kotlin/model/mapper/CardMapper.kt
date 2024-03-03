package model.mapper

import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.american_express
import etong.composeapp.generated.resources.baseline_credit_card_24
import etong.composeapp.generated.resources.dinners_club
import etong.composeapp.generated.resources.discover
import etong.composeapp.generated.resources.jcb
import etong.composeapp.generated.resources.maestro
import etong.composeapp.generated.resources.mastercard
import etong.composeapp.generated.resources.visa
import io.realm.kotlin.types.RealmUUID
import model.CardLogo
import model.CardPaymentUiModel
import model.CardUiModel
import model.storage.Card
import model.storage.CardPayment
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.cardutils.CardType
import utils.cardutils.identifyCardTypeFromCardTypeEnumName

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


@OptIn(ExperimentalResourceApi::class)
fun Card.toUiModel() : CardUiModel {
    val cardType = identifyCardTypeFromCardTypeEnumName(this.cardType)
    val cardLogo = when(cardType) {
        CardType.VISA -> { CardLogo.cardLogoResource(Res.drawable.visa) }
        CardType.JCB -> { CardLogo.cardLogoResource(Res.drawable.jcb) }
        CardType.MASTERCARD -> { CardLogo.cardLogoResource(Res.drawable.mastercard) }
        CardType.MAESTRO -> { CardLogo.cardLogoResource(Res.drawable.maestro) }
        CardType.AMERICAN_EXPRESS -> { CardLogo.cardLogoResource(Res.drawable.american_express) }
        CardType.DINNERS_CLUB -> { CardLogo.cardLogoResource(Res.drawable.dinners_club) }
        CardType.DISCOVER -> { CardLogo.cardLogoResource(Res.drawable.discover) }
        else -> { CardLogo.cardLogoResource(Res.drawable.baseline_credit_card_24) }
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
