package usecase

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.AuthException
import repository.CardRepository

class LoginUserUseCase(
    private val realmApp: App
) {
    suspend operator fun invoke(userIdentification: String, userAuthorization: String) : Boolean {
        val credentials = Credentials.emailPassword(
            email = userIdentification,
            password = userAuthorization
        )
        return try {
            realmApp.login(credentials)
            true
        } catch (e: AuthException) {
            false
        }
    }
}