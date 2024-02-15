package viewmodel

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.UserUiModel
import usecase.GetUserLoginStatusUseCase
import usecase.LoginUserUseCase
import usecase.LogoutUserUseCase
import usecase.RegisterUserUseCase

class UserEnteringScreenModel(
    private val getUserLoginStatusUseCase: GetUserLoginStatusUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
) : ScreenModel {

    private val viewModelJob = SupervisorJob()
    val state = mutableStateOf<UserEnteringScreenState>(UserEnteringScreenState.Idle)

    sealed class UserEnteringScreenState {
        data object Loading : UserEnteringScreenState()
        data object Idle : UserEnteringScreenState()
        data class Failure(
            val errorMessage: String
        ) : UserEnteringScreenState()
        data object Empty : UserEnteringScreenState()
        data class Success(
            val userUiModel: UserUiModel
        ) : UserEnteringScreenState()
    }

    fun startLogin(userIdentification: String, userAuthorization: String) {
        state.value = UserEnteringScreenState.Loading
        screenModelScope.launch {
            val loginSucceed = loginUserUseCase.invoke(
                userIdentification, userAuthorization
            )

            state.value = UserEnteringScreenState.Success(
                UserUiModel(
                    userIdentification = userIdentification,
                    userAuthorization = "",
                    userLoggedIn = loginSucceed
                )
            )
        }
    }

    /**
     * After register, call login function.
     *
     * Ref: https://www.mongodb.com/docs/realm/sdk/kotlin/users/manage-email-password-users/#log-in-or-log-out-a-user
     */
    fun startRegister(userIdentification: String, userAuthorization: String) {
        state.value = UserEnteringScreenState.Loading
        screenModelScope.launch {
            val registerResult = registerUserUseCase.invoke(
                userIdentification, userAuthorization
            )

            registerResult.onSuccess {
                startLogin(userIdentification, userAuthorization)
            }

            registerResult.onFailure {
                state.value = UserEnteringScreenState.Failure(
                    it.message ?: "---"
                )
            }
        }
    }

    fun proceedLogout() {
        state.value = UserEnteringScreenState.Loading
        screenModelScope.launch {
            logoutUserUseCase.invoke()
            delay(5_000L)
            state.value = UserEnteringScreenState.Idle
        }
    }

    override fun onDispose() {
        viewModelJob.cancel()
    }
}