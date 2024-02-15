package repository

import datasource.EtongDatasource
import model.storage.Card
import model.storage.CardPayment

class CardRepository(
    private val etongDatasource: EtongDatasource
) {

    val cardListObserver = etongDatasource.cardObservable()

    suspend fun cardPaymentListObserver(cardIdString: String)
        = etongDatasource.cardPaymentObservable(cardIdString)

    suspend fun tryAddCardToDb(newCard: Card) = etongDatasource.tryAddCardToDb(newCard)

    suspend fun tryDeleteCard(card: Card) = etongDatasource.tryDeleteCard(card)
    suspend fun tryAddCardPaymentToDb(
        newCardPayment: CardPayment
    ) = etongDatasource.tryAddCardPaymentToDb(newCardPayment)

    fun pauseCardSync() {
        etongDatasource.pauseSync()
    }

    fun resumeCardSync() {
        etongDatasource.resumeSync()
    }

    suspend fun reloadRealm() {
        etongDatasource.reloadRealm()
    }

}