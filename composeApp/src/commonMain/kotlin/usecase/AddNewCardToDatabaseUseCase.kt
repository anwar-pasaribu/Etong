package usecase

import model.storage.Card
import repository.CardRepositoryInterface

class AddNewCardToDatabaseUseCase(
    private val repository: CardRepositoryInterface
) {
    suspend operator fun invoke(card: Card) {
        repository.tryAddCardToDb(card)
    }
}