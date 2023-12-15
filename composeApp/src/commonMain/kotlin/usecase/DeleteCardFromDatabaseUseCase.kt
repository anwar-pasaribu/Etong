package usecase

import model.storage.Card
import repository.CardRepository

class DeleteCardFromDatabaseUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(card: Card) {
        repository.tryDeleteCard(card)
    }
}