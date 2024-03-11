package di

import datasource.EtongDatasource
import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import org.koin.dsl.module
import repository.CardRepository
import repository.CardRepositoryInterface
import service.RealmService
import usecase.AddNewCardPaymentToDatabaseUseCase
import usecase.AddNewCardToDatabaseUseCase
import usecase.DeleteCardFromDatabaseUseCase
import usecase.GetUserLoginStatusUseCase
import usecase.LoginUserUseCase
import usecase.LogoutUserUseCase
import usecase.ObserveCardListUseCase
import usecase.ObserveCardPaymentListUseCase
import usecase.RegisterUserUseCase
import usecase.ToggleOnOffSyncUseCase
import viewmodel.CardDetailScreenModel
import viewmodel.CardScreenModel
import viewmodel.HomeScreenModel
import viewmodel.UserEnteringScreenModel

fun appModule() = module {

    single {
        RealmService.realmApp
    }

    single {
        get<App>().currentUser
    }

    single<SyncConfiguration> {
        RealmService.realmSyncConfigBuilder
    }

    single<Realm> { RealmService.realm }
    single { HttpClient() }
    single { EtongDatasource(get(), get(), get()) }
    single<CardRepositoryInterface> { CardRepository(get()) }

    single { ObserveCardListUseCase(get()) }
    single { ObserveCardPaymentListUseCase(get()) }
    single { AddNewCardToDatabaseUseCase(get()) }
    single { ToggleOnOffSyncUseCase(get()) }
    single { DeleteCardFromDatabaseUseCase(get()) }
    single { AddNewCardPaymentToDatabaseUseCase(get()) }
    single { GetUserLoginStatusUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { LogoutUserUseCase(get()) }
    single { RegisterUserUseCase(get()) }

    single { UserEnteringScreenModel(get(), get(), get(), get()) }
    single { HomeScreenModel(get()) }
    single { CardScreenModel(get(), get(), get()) }
    single { CardDetailScreenModel(get(), get(), get()) }

}