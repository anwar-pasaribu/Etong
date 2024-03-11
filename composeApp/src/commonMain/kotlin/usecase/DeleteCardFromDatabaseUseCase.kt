package usecase

import model.storage.Card
import repository.CardRepositoryInterface

class DeleteCardFromDatabaseUseCase(
    private val repository: CardRepositoryInterface
) {
    suspend operator fun invoke(card: Card) {
        repository.tryDeleteCard(card)
    }
}