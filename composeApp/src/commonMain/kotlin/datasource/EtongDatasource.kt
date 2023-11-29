package datasource

import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import model.storage.Card

class EtongDatasource(
    private val httpClient: HttpClient,
    private val realm: Realm
) {

    val databaseObservable: Flow<List<Card>> =
        realm.query<Card>().asFlow().map { changes ->
            changes.list
        }

    suspend fun tryAddToDb(newCard: Card): Unit = withContext(Dispatchers.IO) {
        realm.writeBlocking {
            copyToRealm(newCard)
        }
    }

}