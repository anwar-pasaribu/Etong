package utils.cardutils

fun identifyCardTypeFromNumber(cardNumber: String): CardType {
    val jcbRegex = Regex("^(?:2131|1800|35)[0-9]{0,}$")
    val ameRegex = Regex("^3[47][0-9]{0,}\$")
    val dinersRegex = Regex("^3(?:0[0-59]{1}|[689])[0-9]{0,}\$")
    val visaRegex = Regex("^4[0-9]{0,}\$")
    val masterCardRegex = Regex("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]{0,}\$")
    val maestroRegex = Regex("^(5[06789]|6)[0-9]{0,}\$")
    val discoverRegex =
        Regex("^(6011|65|64[4-9]|62212[6-9]|6221[3-9]|622[2-8]|6229[01]|62292[0-5])[0-9]{0,}\$")

    val trimmedCardNumber = cardNumber.replace(" ", "")

    return when {
        trimmedCardNumber.matches(jcbRegex) -> CardType.JCB
        trimmedCardNumber.matches(ameRegex) -> CardType.AMERICAN_EXPRESS
        trimmedCardNumber.matches(dinersRegex) -> CardType.DINNERS_CLUB
        trimmedCardNumber.matches(visaRegex) -> CardType.VISA
        trimmedCardNumber.matches(masterCardRegex) -> CardType.MASTERCARD
        trimmedCardNumber.matches(discoverRegex) -> CardType.DISCOVER
        trimmedCardNumber.matches(maestroRegex) -> if (cardNumber[0] == '5') CardType.MASTERCARD else CardType.MAESTRO
        else -> CardType.UNKNOWN
    }
}

fun identifyCardTypeFromCardTypeEnumName(cardTypeEnumName: String): CardType {

    return when (cardTypeEnumName) {
        CardType.JCB.name -> CardType.JCB
        CardType.AMERICAN_EXPRESS.name -> CardType.AMERICAN_EXPRESS
        CardType.DINNERS_CLUB.name -> CardType.DINNERS_CLUB
        CardType.VISA.name -> CardType.VISA
        CardType.MASTERCARD.name -> CardType.MASTERCARD
        CardType.DISCOVER.name -> CardType.DISCOVER
        CardType.MAESTRO.name -> CardType.MAESTRO
        else -> CardType.UNKNOWN
    }
}
