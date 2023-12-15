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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        // Open a write transaction
        realm.writeBlocking {
            // Query the Frog type and filter by primary key value
            val cardToDelete: Card? = query<Card>("_id == $0", card._id).first().find()
            // Pass the query results to delete()
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