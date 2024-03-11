package usecase

import repository.CardRepositoryInterface

class ObserveCardPaymentListUseCase(
    private val repository: CardRepositoryInterface
) {
    operator fun invoke(cardIdString: String)
        = repository.cardPaymentListObserver(cardIdString)
}