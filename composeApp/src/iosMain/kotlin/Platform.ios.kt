import config.PlatformType
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: PlatformType = PlatformType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()
actual val Double.formatNominal: String
    get() {
        val nsFormatted = NSNumberFormatter().apply {
            minimumFractionDigits = 0u
            numberStyle = NSNumberFormatterDecimalStyle
        }.stringFromNumber(number = NSNumber(double = this)) ?: this.toString()
        return nsFormatted
    }