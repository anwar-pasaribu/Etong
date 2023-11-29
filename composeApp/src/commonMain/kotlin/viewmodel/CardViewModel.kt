package viewmodel

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.CardUiModel
import model.storage.Card
import usecase.AddNewCardToDatabaseUseCase
import usecase.ObserveCardListUseCase

class CardViewModel(
    private val observeCardListUseCase: ObserveCardListUseCase,
    private val addNewCardToDatabaseUseCase: AddNewCardToDatabaseUseCase
) : ViewModel {

    private val viewModelJob = SupervisorJob()
    override val viewModelScope: CoroutineScope
        = CoroutineScope(viewModelJob + Dispatchers.Main.immediate)
    val state = mutableStateOf<MainScreenState>(MainScreenState.Idle)

    sealed class MainScreenState {
        data object Idle : MainScreenState()
        data object Failure : MainScreenState()
        data object Empty : MainScreenState()
        data class Success(
            val cardList: List<CardUiModel>
        ) : MainScreenState()
    }

    init {
        viewModelScope.launch {
            observeCardListUseCase().collectLatest {
                if (it.isEmpty()) {
                    state.value = MainScreenState.Empty
                } else {
                    state.value = MainScreenState.Success(
                        it.map { card: Card ->
                            card.toUiModel()
                        }
                    )
                }
            }
        }
    }

    fun addNewCard(newCardUiModel: CardUiModel) {
        viewModelScope.launch {
            try {
                addNewCardToDatabaseUseCase(newCardUiModel.toDbInstance())
            } catch (e: Exception) {
                state.value = MainScreenState.Failure
            }
        }
    }

    override fun dispose() {
        viewModelJob.cancel()
    }

    private fun CardUiModel.toDbInstance() : Card {

        val card = Card()
        card.cardNumber = cardNumber
        card.billAmount = billAmount
        card.billMinAmount = billMinAmount
        card.billingDate = billingDate
        card.billDueDate = billDueDate

        return card
    }

    private fun Card.toUiModel() : CardUiModel {
        return CardUiModel(
            cardId = this.id.toString(),
            cardNumber = this.cardNumber,
            billAmount = this.billAmount,
            billMinAmount = this.billMinAmount,
            billingDate = this.billingDate,
            billDueDate = this.billDueDate,
        )
    }
}