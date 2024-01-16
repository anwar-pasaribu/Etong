import config.PlatformType

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform

expect val Double.formatNominal: String