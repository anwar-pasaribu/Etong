package datasource

import di.EtongAppDI
import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.syncSession
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.storage.Card
import model.storage.CardPayment

class EtongDatasource(
    private val httpClient: HttpClient,
    private val realmApp: App,
    private val realm: Realm
) {

    init {
        CoroutineScope(Dispatchers.Main).launch {
            realm.subscriptions.waitForSynchronization()
        }
    }

    fun cardObservable(): Flow<List<Card>> {
        val ownerId = realmApp.currentUser?.id ?: "-"
        return realm
            .query<Card>("ownerId == $0", ownerId)
            .sort("billDueDate", Sort.ASCENDING)
            .asFlow()
            .map { changes ->
                changes.list
            }
    }

    fun cardPaymentObservable(cardIdString: String): Flow<List<CardPayment>> {
        return realm
            .query<CardPayment>("cardId == $0", cardIdString)
            .sort("paymentDate", Sort.DESCENDING)
            .asFlow()
            .map { changes ->
                changes.list
            }
    }

    suspend fun tryAddCardToDb(newCard: Card): Unit = withContext(Dispatchers.IO) {
        val itemWithOwnerId = newCard.apply {
            ownerId = realmApp.currentUser?.id.orEmpty()
        }
        realm.writeBlocking {
            copyToRealm(itemWithOwnerId)
        }
    }

    suspend fun tryAddCardPaymentToDb(payment: CardPayment): Unit = withContext(Dispatchers.IO) {
        realm.writeBlocking {
            copyToRealm(payment)
        }
    }

    suspend fun tryDeleteCard(card: Card): Unit = withContext(Dispatchers.IO) {
        realm.writeBlocking {
            val cardToDelete: Card? = query<Card>("_id == $0", card._id).first().find()
            cardToDelete?.let { delete(it) }
        }
    }

    fun pauseSync() {
        realm.syncSession.pause()
    }

    fun resumeSync() {
        realm.syncSession.resume()
    }

    suspend fun reloadRealm() {
        //
    }
}