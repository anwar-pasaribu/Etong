import android.os.Build
import config.PlatformType
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: PlatformType = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val Double.formatNominal: String
    get() {
        return "%,.0f".format(Locale.GERMAN, this)
    }