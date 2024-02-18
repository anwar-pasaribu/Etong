package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserEnteringScreenModeToggle(
    modifier: Modifier,
    onRegisterMode: () -> Unit,
    onLoginMode: () -> Unit
) {

    var state = remember { mutableIntStateOf(0) }
    val titles = listOf("Login", "Register")
    Card(
        modifier = modifier.then(Modifier.padding(horizontal = 32.dp, vertical = 8.dp)),
    ){
        Column {
            PrimaryTabRow(
                selectedTabIndex = state.intValue,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state.intValue == index,
                        onClick = { state.intValue = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            if (state.intValue == 0) {
                onLoginMode()
            } else {
                onRegisterMode()
            }
        }
    }
}