package preview

import androidx.compose.runtime.Composable
import ui.InputCardDetail
import ui.InputPaidAmountDetail
import ui.theme.EtongTheme
import utils.ThemePreviews


@ThemePreviews
@Composable
fun InputPaidAmountDetailPreview() {
    EtongTheme {
        InputPaidAmountDetail(1000_000_000_000_000.0, {}, {})
    }
}

@ThemePreviews
@Composable
fun InputCardDetailPreview() {
    EtongTheme {
        InputCardDetail({}, {})
    }
}