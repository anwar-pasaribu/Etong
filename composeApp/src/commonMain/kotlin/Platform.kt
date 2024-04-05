import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import config.PlatformType

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform

expect val Double.formatNominal: String


/** Getting screen size info for UI-related calculations */
data class ScreenSizeInfo(val hPX: Int, val wPX: Int, val hDP: Dp, val wDP: Dp)
@Composable
expect fun getScreenSizeInfo(): ScreenSizeInfo