package preview

import androidx.compose.runtime.Composable
import ui.LoadingView
import ui.theme.EtongTheme
import utils.ThemePreviews

@ThemePreviews
@Composable
fun LoadingViewPreview() {
    EtongTheme {
        LoadingView {

        }
    }
}