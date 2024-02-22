package usecase

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.exceptions.AuthException

class RegisterUserUseCase(
    private val realmApp: App
) {
    suspend operator fun invoke(userIdentification: String, userAuthorization: String): Result<Boolean> {

        return try {
            realmApp.emailPasswordAuth.registerUser(
                email = userIdentification,
                password = userAuthorization
            )
            Result.success(true)
        } catch (e: AuthException) {
            Result.failure(e)
        }
    }
}