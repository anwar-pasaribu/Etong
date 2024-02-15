package usecase

import repository.CardRepository

class ObserveCardPaymentListUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(cardIdString: String)
        = repository.cardPaymentListObserver(cardIdString)
}