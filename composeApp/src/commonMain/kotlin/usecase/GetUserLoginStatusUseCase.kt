package usecase

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.subscriptions
import repository.CardRepository

class GetUserLoginStatusUseCase(
    private val realmApp: App
) {
    operator fun invoke() : Boolean {
        return realmApp.currentUser != null
    }
}