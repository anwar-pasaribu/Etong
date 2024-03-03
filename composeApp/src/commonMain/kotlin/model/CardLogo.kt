package model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
data class CardLogo(
    val url: String,
    val resource: DrawableResource
) {
    companion object {
        fun genericLogo(): CardLogo {
            return CardLogo( url = "", resource = DrawableResource("") )
        }

        fun cardLogoResource(drawableRes: DrawableResource): CardLogo {
            return CardLogo( url = "", resource = drawableRes )
        }
    }
}
