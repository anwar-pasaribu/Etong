package usecase

import model.storage.CardPayment
import repository.CardRepositoryInterface

class AddNewCardPaymentToDatabaseUseCase(
    private val repository: CardRepositoryInterface
) {
    suspend operator fun invoke(payment: CardPayment) {
        repository.tryAddCardPaymentToDb(payment)
    }
}