package usecase

import io.realm.kotlin.mongodb.App

class GetUserLoginStatusUseCase(
    private val realmApp: App
) {
    operator fun invoke() : Boolean {
        return realmApp.currentUser != null
    }
}