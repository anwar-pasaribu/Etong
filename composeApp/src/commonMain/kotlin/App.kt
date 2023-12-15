import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.EtongAppDI
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import model.CardUiModel
import ui.EtongTheme
import ui.InputCardDetail
import viewmodel.CardViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun App() {

    val viewModel = remember { EtongAppDI.cardViewModel }

    DisposableEffect(Unit) {
        onDispose { viewModel.dispose() }
    }

    EtongTheme {
        val state = remember { viewModel.state }
        val openAddCardDialog = remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            topBar = {
                TopAppBar(
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 0.dp,
                    title = {
                        Text(
                            "Etong - " + getPlatform().name,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(onClick = { openAddCardDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add CC"
                            )
                        }
                    }
                )
            }
        ) {

            when (val currentState = state.value) {
                is CardViewModel.MainScreenState.Success -> {
                    AnimatedVisibility(true) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            items(items = currentState.cardList, key = { it.cardId }) { cardItem ->
                                CreditCardItem(
                                    modifier = Modifier.animateItemPlacement(),
                                    cardUiModel = cardItem,
                                    onCardRemovalRequest = {
                                        viewModel.removeCard(it)
                                    }
                                )
                            }
                        }
                    }
                }

                CardViewModel.MainScreenState.Empty -> {
                    AnimatedVisibility(true) {
                        Text(
                            text = "Tidak ada Kartu",
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                is CardViewModel.MainScreenState.Failure -> {
                    AnimatedVisibility(true) {
                        Text(
                            text = "Failure - ${currentState.errorMessage}",
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }

                else -> {}
            }

            when {
                openAddCardDialog.value -> {
                    InputCardDetail(
                        onDismissRequest = {
                            openAddCardDialog.value = false
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
fun CardContextMenu(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit,
) {

    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Hapus Kartu")

    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier.size(32.dp, 32.dp),
            onClick = { expanded = true },
            content = {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary,
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(items[index])
                        expanded = false
                    }
                ) {
                    Text(text = s, color = MaterialTheme.colors.error)
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreditCardItem(
    modifier: Modifier = Modifier,
    cardUiModel: CardUiModel,
    onCardRemovalRequest: (CardUiModel) -> Unit
) {
    var selectedCard by remember { mutableStateOf(false) }

    val elevation = if (selectedCard) {
        16.dp
    } else {
        0.dp
    }
    val animatedElevation = animateDpAsState(targetValue = elevation)

    Card(
        modifier = modifier.then(Modifier.fillMaxWidth().heightIn(min = 120.dp, max = 120.dp)),
        elevation = animatedElevation.value,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 0.5.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.combinedClickable(
                onClick = {
                    selectedCard = !selectedCard
                }
            ).padding(all = 8.dp).fillMaxHeight()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("**** - ${cardUiModel.cardNumber}")

                Box(modifier = Modifier.fillMaxWidth()) {
                    CardContextMenu(modifier = Modifier.align(Alignment.CenterEnd)) {
                        onCardRemovalRequest(cardUiModel)
                    }
                }

            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.weight(1f).alignByBaseline(),
                    text = cardUiModel.billMinAmount.formatNominal, fontSize = 24.sp,
                )
                Text(
                    modifier = Modifier.weight(1f).wrapContentWidth(align = Alignment.End)
                        .alignByBaseline(),
                    text = cardUiModel.billAmount.formatNominal,
                    fontSize = 24.sp
                )
            }

            Row(modifier = Modifier.fillMaxHeight()) {
                val tz = TimeZone.currentSystemDefault()

                val todate = Clock.System.now().toLocalDateTime(tz).date

                val dueDateInstant = Instant.fromEpochMilliseconds(cardUiModel.billDueDate)
                val dueDate = dueDateInstant.toLocalDateTime(tz).date
                val dueDateInDays = todate.until(dueDate, DateTimeUnit.DAY)

                val today = todate == dueDate

                val dueDateNumber = dueDate.dayOfMonth
                val dueDateName = dueDate.month.name.lowercase().substring(0, 3).replaceFirstChar {
                    it.uppercase()
                }
                val humanReadableDate = "$dueDateNumber $dueDateName"

                Text(modifier = Modifier.align(Alignment.Bottom), text = "$humanReadableDate")
                Spacer(modifier = Modifier.width(16.dp))

                val formattedDueDateReadable = if (dueDateInDays == 1) {
                    "besok"
                } else if (today) {
                    "hari ini"
                } else if (dueDateInDays < 0) {
                    "lewat ${dueDateInDays.absoluteValue} hari"
                } else {
                    "$dueDateInDays hari lagi"
                }
                Text(modifier = Modifier.align(Alignment.Bottom), text = formattedDueDateReadable)
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