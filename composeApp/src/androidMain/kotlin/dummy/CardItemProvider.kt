package dummy

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import etong.composeapp.generated.resources.Res
import etong.composeapp.generated.resources.visa
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import model.CardLogo
import model.CardUiModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.cardutils.CardType

@OptIn(ExperimentalResourceApi::class)
class CardItemProvider: PreviewParameterProvider<CardUiModel> {
    override val values: Sequence<CardUiModel>
        get() {
            val currentMoment: Instant = Clock.System.now()

            return sequenceOf(
                CardUiModel(
                    cardId = "id-1",
                    cardLabel = "Visa CIMB - in 2 days ya",
                    cardType = CardType.VISA,
                    cardLogo = CardLogo.cardLogoResource(Res.drawable.visa),
                    billAmount = 236_656_000.0,
                    billMinAmount = 1_245_000.0,
                    billingDate = currentMoment.toEpochMilliseconds(),
                    billDueDate = currentMoment.plus(DatePeriod(days = 2), TimeZone.currentSystemDefault()).toEpochMilliseconds()
                )
            )
        }
}