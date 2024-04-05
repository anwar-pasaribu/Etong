import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import config.PlatformType
import java.util.Locale

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val type: PlatformType
        get() = PlatformType.DESKTOP
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val Double.formatNominal: String
    get() {
        return "%,.0f".format(Locale.GERMAN, this)
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenSizeInfo(): ScreenSizeInfo {
    val config = LocalWindowInfo.current.containerSize

    val density = LocalDensity.current
    val hDp = config.height.dp
    val wDp = config.width.dp

    return remember(density, config) {
        ScreenSizeInfo(
            hPX = with(density) { hDp.roundToPx() },
            wPX = with(density) { wDp.roundToPx() },
            hDP = hDp,
            wDP = wDp
        )
    }
}