package repository

import datasource.EtongDatasource
import model.storage.Card
import model.storage.CardPayment

class CardRepository(
    private val etongDatasource: EtongDatasource
): CardRepositoryInterface {

    override val cardListObserver = etongDatasource.cardObservable()

    override fun cardPaymentListObserver(cardIdString: String)
        = etongDatasource.cardPaymentObservable(cardIdString)

    override suspend fun tryAddCardToDb(newCard: Card) = etongDatasource.tryAddCardToDb(newCard)

    override suspend fun tryDeleteCard(card: Card) = etongDatasource.tryDeleteCard(card)
    override suspend fun tryAddCardPaymentToDb(
        newCardPayment: CardPayment
    ) = etongDatasource.tryAddCardPaymentToDb(newCardPayment)

    override fun pauseCardSync() {
        etongDatasource.pauseSync()
    }

    override fun resumeCardSync() {
        etongDatasource.resumeSync()
    }

    override suspend fun reloadRealm() {
        etongDatasource.reloadRealm()
    }

}