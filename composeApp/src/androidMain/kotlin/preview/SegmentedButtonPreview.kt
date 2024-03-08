package preview

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.UserEnteringScreenModeToggle
import ui.theme.EtongTheme

@Preview(showBackground = true)
@Composable
fun SegmentedButtonPreview() {
    EtongTheme {
        UserEnteringScreenModeToggle(
            modifier = Modifier.padding(top = 32.dp),
            onRegisterMode = {}
        ) {}
    }
}