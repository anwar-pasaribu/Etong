package usecase

import model.storage.CardPayment
import repository.CardRepository

class AddNewCardPaymentToDatabaseUseCase(
    private val repository: CardRepository
) {
    suspend operator fun invoke(payment: CardPayment) {
        repository.tryAddCardPaymentToDb(payment)
    }
}