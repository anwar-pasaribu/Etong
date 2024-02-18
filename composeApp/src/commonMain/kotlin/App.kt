import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ui.screen.HomeScreen
import ui.theme.EtongTheme

@Composable
fun App() {
    EtongTheme {
        Navigator(screen = HomeScreen())
    }
}