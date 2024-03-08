package preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import dummy.CardItemProvider
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
import ui.CreditCardItem
import ui.TotalBillCardItem
import ui.theme.EtongTheme
import utils.ThemePreviews
import utils.cardutils.CardType

@ThemePreviews
@Composable
fun CreditCardItemPreview(
    @PreviewParameter(CardItemProvider::class) cardUiModel: CardUiModel
) {
    EtongTheme {
        CreditCardItem(cardUiModel = cardUiModel) {}
    }
}

@OptIn(ExperimentalResourceApi::class)
@ThemePreviews
@Composable
fun TotalBillCardItemPreview() {
    EtongTheme {
        val currentMoment: Instant = Clock.System.now()

        val card = CardUiModel(
            cardId = "",
            cardLabel = "",
            cardType = CardType.VISA,
            cardLogo = CardLogo.cardLogoResource(Res.drawable.visa),
            billAmount = 23_656_000.0,
            billMinAmount = 1_245_000.0,
            billingDate = currentMoment.toEpochMilliseconds(),
            billDueDate = currentMoment.plus(DatePeriod(days = 2), TimeZone.currentSystemDefault()).toEpochMilliseconds()
        )
        TotalBillCardItem(cardUiModel = card)
    }
}