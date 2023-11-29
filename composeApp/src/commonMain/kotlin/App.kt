import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.EtongAppDI
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.CardUiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.InputCardDetail
import viewmodel.CardViewModel

@OptIn(ExperimentalResourceApi::class, ExperimentalComposeUiApi::class)
@Composable
fun App() {

    val viewModel = remember { EtongAppDI.cardViewModel }

    DisposableEffect(Unit) {
        onDispose { viewModel.dispose() }
    }

    MaterialTheme {
        val state = remember { viewModel.state }
        var greetingText by remember { mutableStateOf("Hello World!") }
        var showImage by remember { mutableStateOf(false) }
        val openAlertDialog = remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                var text by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Label") },
                    placeholder = { Text("Placeholder") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )

                Spacer(Modifier.width(16.dp))

                AnimatedVisibility(!openAlertDialog.value) {
                    Button(onClick = {
                        text = ""
                        openAlertDialog.value = true
                    }) {
                        Text("Add")
                    }
                }
            }

            when (val currentState = state.value) {
                is CardViewModel.MainScreenState.Success -> {
                    AnimatedVisibility(true) {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),

                            ) {
                            items(currentState.cardList) { cardItem ->
                                CreditCardItem(cardItem)
                            }
                        }
                    }
                }
                CardViewModel.MainScreenState.Empty -> {
                    AnimatedVisibility(true) {
                        Text(text = "Tidak ada Kartu", style = MaterialTheme.typography.h4)
                    }
                }
                CardViewModel.MainScreenState.Failure -> {

                }
                else -> {}
            }

            when {
                openAlertDialog.value -> {
                    InputCardDetail(
                        onDismissRequest = {
                            openAlertDialog.value = false
                        },
                        onSubmitRequest = { newCard ->
                            viewModel.addNewCard(newCard)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CreditCardItem(cardUiModel: CardUiModel) {
    var selectedCard by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val radius = if (selectedCard) {
        16.dp
    } else {
        0.dp
    }
    val cornerRadius = animateDpAsState(targetValue = radius)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = cornerRadius.value,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = rememberRipple()
            ) {
                selectedCard = !selectedCard
            }.padding(all = 8.dp)
        ) {
            Row {
                Text("bank_ic")
                Spacer(modifier = Modifier.width(16.dp))
                Text("**** - ${cardUiModel.cardNumber}")
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f).alignByBaseline(),
                    text = "Rp ${cardUiModel.billMinAmount}", fontSize = 24.sp,
                )
                Text(
                    modifier = Modifier.weight(1f).wrapContentWidth(align = Alignment.End)
                        .alignByBaseline(), text = "Rp ${cardUiModel.billAmount}", fontSize = 32.sp
                )
            }

            Row {
                Text("${cardUiModel.billingDate}")
                Spacer(modifier = Modifier.width(16.dp))
                Text("${cardUiModel.billDueDate}")
            }
        }
    }
}

fun todaysDate(): String {
    fun LocalDateTime.format() = toString().substringBefore('T')

    val now = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(zone).format()
}