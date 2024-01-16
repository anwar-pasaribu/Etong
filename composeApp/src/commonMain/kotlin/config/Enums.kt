package config

enum class PlatformType {
    IOS, ANDROID, DESKTOP
}

enum class Locales(val value: String) {
    ARABIC(ARABIC_LANGUAGE), ENGLISH(ENGLISH_LANGUAGE)
}