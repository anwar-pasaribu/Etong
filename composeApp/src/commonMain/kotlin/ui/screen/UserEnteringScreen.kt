package ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject
import ui.LoadingView
import ui.UserEnteringScreenModeToggle
import viewmodel.UserEnteringScreenModel

data class UserEnteringScreen(var logoutRequested: Boolean = false) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = koinInject<UserEnteringScreenModel> ()
        val state = remember { screenModel.state }

        val loginFailed = remember { mutableStateOf(false) }
        val loadingMode = remember { mutableStateOf(false) }
        val errorMessage = remember { mutableStateOf("") }

        if (logoutRequested) {
            screenModel.proceedLogout()
            logoutRequested = false
        }

        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        ) {
            when (val currentState = state.value) {
                is UserEnteringScreenModel.UserEnteringScreenState.Loading -> {
                    loginFailed.value = false
                    loadingMode.value = true
                    errorMessage.value = ""
                }

                is UserEnteringScreenModel.UserEnteringScreenState.Success -> {
                    loginFailed.value = false
                    loadingMode.value = false
                    errorMessage.value = ""
                    val loginSucceed = currentState.userUiModel.userLoggedIn
                    if (loginSucceed) {
                        navigator.replaceAll(CardListScreen())
                    }
                }

                is UserEnteringScreenModel.UserEnteringScreenState.Failure -> {
                    loginFailed.value = true
                    loadingMode.value = false
                    errorMessage.value = currentState.errorMessage
                }

                is UserEnteringScreenModel.UserEnteringScreenState.LoginFailure -> {
                    loginFailed.value = true
                    loadingMode.value = false
                    errorMessage.value = currentState.msg
                }

                is UserEnteringScreenModel.UserEnteringScreenState.Idle -> {
                    loginFailed.value = false
                    loadingMode.value = false
                    errorMessage.value = ""
                }
            }

            LoginForm(
                modifier = Modifier.align(Alignment.TopCenter),
                loginFailed = loginFailed,
                loadingMode = loadingMode,
                errorMessage = errorMessage,
                proceedLogin = { userIdentification, userAuthorization ->
                    loginFailed.value = false
                    screenModel.startLogin(
                        userIdentification, userAuthorization
                    )
                },
                proceedRegister = { userIdentification, userAuthorization ->
                    loginFailed.value = false
                    screenModel.startRegister(
                        userIdentification, userAuthorization
                    )
                }
            )
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun LoginForm(
        modifier: Modifier,
        loginFailed: MutableState<Boolean>,
        loadingMode: State<Boolean>,
        errorMessage: MutableState<String>,
        proceedLogin: (String, String) -> Unit,
        proceedRegister: (String, String) -> Unit
    ) {

        val loginMode = remember { mutableStateOf(true) }
        val ctaActionButtonText = remember { mutableStateOf("Login") }

        Column {
            Spacer(
                Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars
                )
            )

            UserEnteringScreenModeToggle(
                modifier = Modifier.padding(top = 32.dp),
                onRegisterMode = {
                    ctaActionButtonText.value = "Register"
                    loginMode.value = false
                    loginFailed.value = false
                    errorMessage.value = ""
                },
                onLoginMode = {
                    ctaActionButtonText.value = "Login"
                    loginMode.value = true
                    loginFailed.value = false
                    errorMessage.value = ""
                }
            )

            Card(
                modifier = modifier.then(Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var userIdentification by remember { mutableStateOf("asa@mail.com") }
                    var userAuthorization by remember { mutableStateOf("asa123") }
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = userIdentification,
                        onValueChange = { userIdentification = it },
                        placeholder = { Text(text = "Email") },
                        label = { Text(text = "Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = userAuthorization,
                        onValueChange = {
                            userAuthorization = it
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = { Text(text = "Password") },
                        label = { Text(text = "Password") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            val description =
                                if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Crossfade(targetState = passwordVisible) {
                                    Icon(
                                        if (it) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        description
                                    )
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    AnimatedVisibility(visible = loginFailed.value) {
                        Text(
                            text = "Terjadi kesalahan, periksa email/password dan coba lagi",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    AnimatedVisibility(visible = errorMessage.value.isNotEmpty()) {
                        Text(
                            text = errorMessage.value,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    AnimatedVisibility(true) {
                        if (loadingMode.value) {
                            Column {
                                LoadingView()
                            }
                        } else {
                            val buttonEnabled = userIdentification.isNotEmpty()
                                    && userAuthorization.isNotEmpty()
                            Button(
                                onClick = {
                                    if (loginMode.value) {
                                        proceedLogin(userIdentification, userAuthorization)
                                    } else {
                                        proceedRegister(userIdentification, userAuthorization)
                                    }
                                },
                                enabled = buttonEnabled
                            ) {
                                AnimatedContent(
                                    targetState = ctaActionButtonText.value,
                                    transitionSpec = {
                                        EnterTransition.None togetherWith ExitTransition.None
                                    }
                                ) { text ->
                                    Text(
                                        text = text,
                                        modifier = Modifier.animateEnterExit(
                                            enter = scaleIn(),
                                            exit = scaleOut()
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}