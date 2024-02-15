package viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import model.UserUiModel
import usecase.GetUserLoginStatusUseCase

class HomeScreenModel(
    private val getUserLoginStatusUseCase: GetUserLoginStatusUseCase,
) : ScreenModel {

    private val viewModelJob = SupervisorJob()
    val state = mutableStateOf<HomeScreenState>(HomeScreenState.Idle)

    sealed class HomeScreenState {
        data object Idle : HomeScreenState()
        data class Success(
            val userUiModel: UserUiModel
        ) : HomeScreenState()
    }

    init {
        screenModelScope.launch {
            val loginSucceed = getUserLoginStatusUseCase.invoke()

            state.value = HomeScreenState.Success(
                UserUiModel(
                    userIdentification = "",
                    userAuthorization = "",
                    userLoggedIn = loginSucceed
                )
            )
        }
    }

    override fun onDispose() {
        viewModelJob.cancel()
    }
}