package viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import model.CardLogo
import model.CardUiModel
import model.mapper.toDbInstance
import model.mapper.toUiModel
import model.storage.Card
import usecase.AddNewCardToDatabaseUseCase
import usecase.ObserveCardListUseCase
import usecase.ToggleOnOffSyncUseCase
import utils.cardutils.CardType

class CardScreenModel(
    private val toggleOnOffSyncUseCase: ToggleOnOffSyncUseCase,
    private val observeCardListUseCase: ObserveCardListUseCase,
    private val addNewCardToDatabaseUseCase: AddNewCardToDatabaseUseCase,
) : ScreenModel {

    private val viewModelJob = SupervisorJob()
    val state = mutableStateOf<MainScreenState>(MainScreenState.Idle)

    sealed class MainScreenState {
        data object Loading : MainScreenState()
        data object Idle : MainScreenState()
        data class Failure(
            val errorMessage: String
        ) : MainScreenState()
        data object Empty : MainScreenState()
        data class Success(
            val cardList: List<CardUiModel>,
            val totalBillCard: CardUiModel,
        ) : MainScreenState()
    }

    init {
        toggleOnOffSyncUseCase.invoke(on = true)
        loadCardList()
    }

    fun loadCardList() {
        state.value = MainScreenState.Loading
        screenModelScope.launch {
            observeCardListUseCase().collectLatest { cardList ->
                if (cardList.isEmpty()) {
                    state.value = MainScreenState.Empty
                } else {
                    state.value = MainScreenState.Success(
                        cardList.map { card: Card ->
                            card.toUiModel()
                        },
                        CardUiModel(
                            cardId = "",
                            cardLabel = "",
                            billAmount = cardList.sumOf { it.billAmount },
                            billMinAmount = cardList.sumOf { it.billMinAmount },
                            billDueDate = 0L,
                            billingDate = 0L,
                            cardLogo = CardLogo.genericLogo(),
                            cardType = CardType.UNKNOWN
                        )
                    )
                }
            }
        }
    }

    fun addNewCard(newCardUiModel: CardUiModel) {
        screenModelScope.launch {
            try {
                addNewCardToDatabaseUseCase(newCardUiModel.toDbInstance())
            } catch (e: Exception) {
                state.value = MainScreenState.Failure(errorMessage = e.message ?: "Exception")
            }
        }
    }

    override fun onDispose() {
        toggleOnOffSyncUseCase.invoke(on = false)
        viewModelJob.cancel()
    }
}