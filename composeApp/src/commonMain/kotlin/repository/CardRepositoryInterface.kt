package repository

import kotlinx.coroutines.flow.Flow
import model.storage.Card
import model.storage.CardPayment

interface CardRepositoryInterface {
    val cardListObserver: Flow<List<Card>>
    fun cardPaymentListObserver(cardIdString: String):  Flow<List<CardPayment>>
    suspend fun tryAddCardToDb(newCard: Card)
    suspend fun tryDeleteCard(card: Card)
    suspend fun tryAddCardPaymentToDb(
        newCardPayment: CardPayment
    )
    fun pauseCardSync()
    fun resumeCardSync()
    suspend fun reloadRealm()
}