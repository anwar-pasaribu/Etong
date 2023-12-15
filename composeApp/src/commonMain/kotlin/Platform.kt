interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val Double.formatNominal: String