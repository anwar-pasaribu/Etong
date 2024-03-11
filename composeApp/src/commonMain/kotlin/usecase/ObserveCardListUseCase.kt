package usecase

import repository.CardRepositoryInterface

class ObserveCardListUseCase(
    private val repository: CardRepositoryInterface
) {
    operator fun invoke() = repository.cardListObserver
}