package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Suppress("UNUSED_PARAMETER")
@Composable
fun InputDateManual(
    modifier: Modifier = Modifier,
    onInputFinished: (String) -> Unit
) {

    var dayDd by remember { mutableStateOf("") }
    var monthMm by remember { mutableStateOf("") }
    var yearYy by remember { mutableStateOf("") }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        val dayItems = mutableListOf<String>()
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        for (dayIndex in 0..31) {
            if (dayIndex == 0) {
                dayItems.add("Tgl")
            } else {
                dayItems.add(dayIndex.toString())
            }
        }
        val monthList = listOf(
            "Bulan",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "Mei",
            "Jun",
            "Jul",
            "Agu",
            "Sep",
            "Okt",
            "Nov",
            "Des"
        )

        DropdownSection(
            modifier = Modifier.weight(1F).fillMaxWidth(),
            items = dayItems
        ) { day ->
            dayDd = day.padStart(2, '0')
        }

        DropdownSection(
            modifier = Modifier.weight(1F),
            items = monthList
        ) { monthName ->
            monthMm = monthList.indexOf(monthName).toString().padStart(2, '0')
        }

        val yearString = today.year.toString()
        DropdownSection(
            modifier = Modifier.weight(1F),
            items = listOf("Tahun", yearString)
        ) {
            yearYy = it
        }

        if (dayDd.isNotEmpty() && monthMm.isNotEmpty() && yearYy.isNotEmpty()) {
            onInputFinished("$yearString-$monthMm-${dayDd}T00:00:00")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DropdownSection(
    modifier: Modifier = Modifier,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier.then(
            Modifier
                .clip(MaterialTheme.shapes.small)
                .clickable(
                    onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        expanded = true
                    }
                )
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.shapes.small
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .fillMaxWidth()
        )
    ) {
        AnimatedContent(
            targetState = items[selectedIndex],
            transitionSpec = {
                EnterTransition.None togetherWith ExitTransition.None
            }
        ) { targetCount ->
            Text(
                text = targetCount,
                modifier = Modifier.animateEnterExit(
                    enter = scaleIn(),
                    exit = scaleOut()
                )
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                val itemLabel = item
                val itemSelected = index != 0 && selectedIndex == index
                DropdownMenuItem(
                    onClick = {
                        onItemSelected(items[index])
                        selectedIndex = index
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(),
                    enabled = index != 0,
                    text = {
                        val fontStyle = if (index == 0) {
                            MaterialTheme.typography.bodyMedium
                        } else {
                            MaterialTheme.typography.bodyLarge
                        }
                        val fontWeight = if (itemSelected) {
                            androidx.compose.ui.text.font.FontWeight.Bold
                        } else {
                            androidx.compose.ui.text.font.FontWeight.Normal
                        }
                        Text(
                            text = itemLabel,
                            style = fontStyle,
                            fontWeight = fontWeight
                        )
                    }
                )
            }
        }
    }
}