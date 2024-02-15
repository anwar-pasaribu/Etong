package usecase

import model.storage.Card
import repository.CardRepository

class AddNewCardToDatabaseUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(card: Card) {
        repository.tryAddCardToDb(card)
    }
}