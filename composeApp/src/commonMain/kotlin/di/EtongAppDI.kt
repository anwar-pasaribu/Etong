package di

import datasource.EtongDatasource
import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking
import model.storage.Card
import repository.CardRepository
import usecase.AddNewCardToDatabaseUseCase
import usecase.DeleteCardFromDatabaseUseCase
import usecase.GetUserLoginStatusUseCase
import usecase.ObserveCardListUseCase
import usecase.ToggleOnOffSyncUseCase
import viewmodel.CardScreenModel
import viewmodel.CardViewModel

object EtongAppDI {

    private val realmApp by lazy {
        val app = App.create(
            AppConfiguration.Builder("devicesync-sutxw")
                .baseUrl("https://realm.mongodb.com")
                .build()
        )
        app
    }

    private val realmSyncConfigBuilder: SyncConfiguration by lazy {
        SyncConfiguration.Builder(currentUser, setOf(Card::class))
            .initialSubscriptions { realm ->
                add(realm.query<Card>())
            }
            .waitForInitialRemoteData()
            .build()
    }

    private val currentUser: User
        get() {
            if (realmApp.currentUser != null) return realmApp.currentUser!!
            else {
                return runBlocking {
                    return@runBlocking realmApp.login(
                        Credentials.emailPassword(
                            "asa@mail.com",
                            "asa123")
                    )
                }
            }
        }

    private val realm by lazy {
        Realm.open(
            realmSyncConfigBuilder
        )
    }
    private val httpClient by lazy { HttpClient() }
    private val etongDatasource by lazy {
        EtongDatasource(httpClient, realmApp, realm)
    }
    private val cardRepository by lazy {
        CardRepository(etongDatasource)
    }

    private val observeCardListUseCase by lazy {
        ObserveCardListUseCase(cardRepository)
    }
    private val addNewCardToDatabaseUseCase by lazy {
        AddNewCardToDatabaseUseCase(cardRepository)
    }
    private val toggleOnOffSyncUseCase by lazy {
        ToggleOnOffSyncUseCase(cardRepository)
    }

    private val getUserLoginStatusUseCase by lazy {
        GetUserLoginStatusUseCase(realmApp)
    }

    private val deleteCardFromDatabaseUseCase by lazy {
        DeleteCardFromDatabaseUseCase(cardRepository)
    }

    val cardViewModel by lazy {
        CardViewModel(
            toggleOnOffSyncUseCase,
            observeCardListUseCase,
            addNewCardToDatabaseUseCase,
            deleteCardFromDatabaseUseCase,
        )
    }

    val cardScreenModel by lazy {
        CardScreenModel(
            toggleOnOffSyncUseCase,
            observeCardListUseCase,
            addNewCardToDatabaseUseCase,
            deleteCardFromDatabaseUseCase,
        )
    }
}