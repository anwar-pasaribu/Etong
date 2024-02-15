package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ){
        Column(modifier = Modifier.clip(MaterialTheme.shapes.medium)) {
            PrimaryTabRow(
                selectedTabIndex = state.intValue,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                divider = {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .height(0.dp)
                    )
                }
            ) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state.intValue == index,
                        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                        onClick = { state.intValue = index },
                        text = {
                            Text(
                                text = title,
                                maxLines = 2,
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