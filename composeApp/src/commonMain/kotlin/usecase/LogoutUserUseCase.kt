package usecase

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.exceptions.AuthException

class LogoutUserUseCase(
    private val realmApp: App
) {
    suspend operator fun invoke() : Boolean {
        return try {
            realmApp.currentUser?.logOut()
            true
        } catch (e: AuthException) {
            false
        } finally {
            realmApp.currentUser?.remove()
        }
    }
}