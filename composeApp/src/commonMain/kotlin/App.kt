import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import config.PlatformType
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.EtongTheme
import ui.screen.CardListScreen

@Composable
fun App() {

    EtongTheme {
        Navigator(screen = CardListScreen()) { navigator ->
            val supportSwipeBack = remember { getPlatform().type == PlatformType.IOS }
            if (supportSwipeBack) {
                SlideTransition(navigator = navigator)
            } else {
                FadeTransition(navigator = navigator)
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