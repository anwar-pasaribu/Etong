package viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.CardPaymentUiModel
import model.CardUiModel
import model.mapper.toDbInstance
import model.mapper.toUiModel
import model.storage.CardPayment
import usecase.AddNewCardPaymentToDatabaseUseCase
import usecase.DeleteCardFromDatabaseUseCase
import usecase.ObserveCardPaymentListUseCase

class CardDetailScreenModel(
    private val addNewCardPaymentToDatabaseUseCase: AddNewCardPaymentToDatabaseUseCase,
    private val deleteCardFromDatabaseUseCase: DeleteCardFromDatabaseUseCase,
    private val observeCardPaymentListUseCase: ObserveCardPaymentListUseCase,
) : ScreenModel {

    private val viewModelJob = SupervisorJob()
    val state = mutableStateOf<CardDetailScreenState>(CardDetailScreenState.Idle)

    sealed class CardDetailScreenState {
        data object Loading : CardDetailScreenState()
        data object Idle : CardDetailScreenState()
        data class Failure(
            val errorMessage: String
        ) : CardDetailScreenState()
        data object Empty : CardDetailScreenState()
        data object DeleteSuccess : CardDetailScreenState()
        data class Success(
            val cardPaymentList: List<CardPaymentUiModel>,
            val paidAmount: Double = 1.0
        ) : CardDetailScreenState()
    }

    fun observeCardPayment(card: CardUiModel) {
        state.value = CardDetailScreenState.Loading
        screenModelScope.launch {
            observeCardPaymentListUseCase(card.cardId).collectLatest { it ->
                if (it.isEmpty()) {
                    state.value = CardDetailScreenState.Empty
                } else {
                    state.value = CardDetailScreenState.Success(
                        cardPaymentList = it.map { cardPayment: CardPayment ->
                            cardPayment.toUiModel()
                        },
                        paidAmount = it.sumOf { cardPayment: CardPayment -> cardPayment.amount }
                    )
                }
            }
        }
    }

    fun addNewCardPayment(cardPaymentUiModel: CardPaymentUiModel) {
        screenModelScope.launch {
            try {
                addNewCardPaymentToDatabaseUseCase(cardPaymentUiModel.toDbInstance())
            } catch (e: Exception) {
                state.value = CardDetailScreenState.Failure(errorMessage = e.message ?: "Exception")
            }
        }
    }

    fun removeCard(card: CardUiModel) {
        screenModelScope.launch {
            try {
                deleteCardFromDatabaseUseCase(card.toDbInstance())
            } catch (e: Exception) {
                state.value = CardDetailScreenState.Failure(errorMessage = e.message ?: "Exception")
            } finally {
                state.value = CardDetailScreenState.DeleteSuccess
            }
        }
    }

    override fun onDispose() {
        viewModelJob.cancel()
    }
}