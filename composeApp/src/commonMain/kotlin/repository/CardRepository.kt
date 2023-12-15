package repository

import datasource.EtongDatasource
import model.storage.Card

class CardRepository(
    private val etongDatasource: EtongDatasource
) {

    val cardListObserver = etongDatasource.databaseObservable

    suspend fun tryAddToDb(newCard: Card) = etongDatasource.tryAddToDb(newCard)

    suspend fun tryDeleteCard(card: Card) = etongDatasource.tryDeleteCard(card)

    fun pauseCardSync() {
        etongDatasource.pauseSync()
    }

    fun resumeCardSync() {
        etongDatasource.resumeSync()
    }

}