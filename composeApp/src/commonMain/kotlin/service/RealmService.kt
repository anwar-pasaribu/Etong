package service

import com.unwur.etong.BuildKonfig
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import model.storage.Card
import model.storage.CardPayment

object RealmService {

    val realmApp by lazy {
        val atlasAppId = BuildKonfig.ATLAS_APP_ID
        val atlasBaseUrl = BuildKonfig.ATLAS_BASE_URL
        App.create(
            AppConfiguration.Builder(atlasAppId)
                .baseUrl(atlasBaseUrl)
                .build()
        )
    }

    val realmSyncConfigBuilder: SyncConfiguration by lazy {
        SyncConfiguration.Builder(currentUser, setOf(Card::class, CardPayment::class))
            .name("etong-realm-db")
            .initialSubscriptions { realm ->
                add(realm.query<Card>("ownerId == $0", currentUser.id))
                add(realm.query<CardPayment>())
            }
            .log(LogLevel.DEBUG)  // TODO Remove before release
            .build()
    }

    val currentUser: User
        get() {
            return realmApp.currentUser!!
        }

    val realm by lazy {
        Realm.open(
            realmSyncConfigBuilder
        )
    }
}