package di

import datasource.EtongDatasource
import io.ktor.client.HttpClient
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import model.storage.Card
import repository.CardRepository
import usecase.AddNewCardToDatabaseUseCase
import usecase.ObserveCardListUseCase
import viewmodel.CardViewModel

object EtongAppDI {

    private val realm by lazy {
        Realm.open(
            RealmConfiguration.create(
                schema = setOf(
                    Card::class
                )
            )
        )
    }
    private val httpClient by lazy { HttpClient() }
    private val etongDatasource by lazy {
        EtongDatasource(httpClient, realm)
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

    val cardViewModel by lazy {
        CardViewModel(
            observeCardListUseCase,
            addNewCardToDatabaseUseCase
        )
    }
}