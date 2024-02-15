package usecase

import repository.CardRepository

class ReloadRealmDatabaseUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke() {
        repository.reloadRealm()
    }
}