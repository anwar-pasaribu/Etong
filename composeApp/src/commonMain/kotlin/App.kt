import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import di.appModule
import org.koin.compose.KoinApplication
import ui.screen.HomeScreen
import ui.theme.EtongTheme

@Composable
fun App() {
    KoinApplication(moduleList = { listOf(appModule()) }) {
        EtongTheme {
            Navigator(screen = HomeScreen())
        }
    }
}