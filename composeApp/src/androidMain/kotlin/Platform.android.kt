import android.os.Build
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val Double.formatNominal: String
    get() {
        return "%,.0f".format(Locale.GERMAN, this).prependIndent("Rp ")
    }