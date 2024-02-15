package di

import datasource.EtongDatasource
import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import model.storage.Card
import model.storage.CardPayment
import repository.CardRepository
import usecase.AddNewCardPaymentToDatabaseUseCase
import usecase.AddNewCardToDatabaseUseCase
import usecase.DeleteCardFromDatabaseUseCase
import usecase.GetUserLoginStatusUseCase
import usecase.LoginUserUseCase
import usecase.LogoutUserUseCase
import usecase.ObserveCardListUseCase
import usecase.ObserveCardPaymentListUseCase
import usecase.RegisterUserUseCase
import usecase.ReloadRealmDatabaseUseCase
import usecase.ToggleOnOffSyncUseCase
import viewmodel.CardDetailScreenModel
import viewmodel.CardScreenModel
import viewmodel.HomeScreenModel
import viewmodel.UserEnteringScreenModel

object EtongAppDI {

    private val realmApp by lazy {
        val app = App.create(
            AppConfiguration.Builder("devicesync-sutxw")
                .baseUrl("https://services.cloud.mongodb.com")
                .build()
        )
        app
    }

    private val realmSyncConfigBuilder: SyncConfiguration by lazy {
        SyncConfiguration.Builder(currentUser, setOf(Card::class, CardPayment::class))
            .name("etong-realm-db")
            .initialSubscriptions { realm ->
                add(realm.query<Card>("ownerId == $0", currentUser.id))
                add(realm.query<CardPayment>())
            }
            .log(LogLevel.ALL)  // TODO Remove before release
            .waitForInitialRemoteData()
            .build()
    }

    private val currentUser: User
        get() {
            return realmApp.currentUser!!
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

    private val observeCardPaymentListUseCase by lazy {
        ObserveCardPaymentListUseCase(cardRepository)
    }

    private val addNewCardToDatabaseUseCase by lazy {
        AddNewCardToDatabaseUseCase(cardRepository)
    }
    private val toggleOnOffSyncUseCase by lazy {
        ToggleOnOffSyncUseCase(cardRepository)
    }

    private val deleteCardFromDatabaseUseCase by lazy {
        DeleteCardFromDatabaseUseCase(cardRepository)
    }

    private val addNewCardPaymentToDatabaseUseCase by lazy {
        AddNewCardPaymentToDatabaseUseCase(cardRepository)
    }

    private val getUserLoginStatusUseCase by lazy {
        GetUserLoginStatusUseCase(realmApp)
    }

    private val loginUserUseCase by lazy {
        LoginUserUseCase(realmApp)
    }

    private val logoutUserUseCase by lazy {
        LogoutUserUseCase(realmApp)
    }

    private val registerUserUseCase by lazy {
        RegisterUserUseCase(realmApp)
    }

    private val reloadRealmDatabaseUseCase by lazy {
        ReloadRealmDatabaseUseCase(cardRepository)
    }

    val userEnteringScreenModel by lazy {
        UserEnteringScreenModel(
            getUserLoginStatusUseCase,
            loginUserUseCase,
            logoutUserUseCase,
            registerUserUseCase
        )
    }

    val homeScreenModel by lazy {
        HomeScreenModel(
            getUserLoginStatusUseCase,
        )
    }

    val cardScreenModel by lazy {
        CardScreenModel(
            toggleOnOffSyncUseCase,
            observeCardListUseCase,
            addNewCardToDatabaseUseCase,
            deleteCardFromDatabaseUseCase,
            reloadRealmDatabaseUseCase
        )
    }

    val cardDetailScreenModel by lazy {
        CardDetailScreenModel(
            addNewCardPaymentToDatabaseUseCase = addNewCardPaymentToDatabaseUseCase,
            deleteCardFromDatabaseUseCase = deleteCardFromDatabaseUseCase,
            observeCardPaymentListUseCase = observeCardPaymentListUseCase,
        )
    }
}