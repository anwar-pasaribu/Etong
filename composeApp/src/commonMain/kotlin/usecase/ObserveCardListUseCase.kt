package usecase

import repository.CardRepository

class ObserveCardListUseCase(
    private val repository: CardRepository
) {
    operator fun invoke() = repository.cardListObserver
}