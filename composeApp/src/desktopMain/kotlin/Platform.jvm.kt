import java.util.Locale

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val Double.formatNominal: String
    get() {
        return "%,.0f".format(Locale.GERMAN, this).prependIndent("Rp ")
    }