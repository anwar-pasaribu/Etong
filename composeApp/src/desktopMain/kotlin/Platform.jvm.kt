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