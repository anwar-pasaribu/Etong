package datasource

import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.syncSession
import io.realm.kotlin.query.find
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.delayFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.storage.Card

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

    val databaseObservable: Flow<List<Card>> =
        realm
            .query<Card>()
            .asFlow()
            .onStart {
                delay(2000L)
            }
            .map { changes ->
                changes.list
            }

    suspend fun tryAddToDb(newCard: Card): Unit = withContext(Dispatchers.IO) {
        val itemWithOwnerId = newCard.apply {
            ownerId = realmApp.currentUser?.id.orEmpty()
        }
        realm.writeBlocking {
            copyToRealm(itemWithOwnerId)
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

}