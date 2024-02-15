package theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EtongTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xff1f2020),
            onPrimary = Color(0xff00c4b1),
            primaryContainer = Color(0xff1f2020),
            onPrimaryContainer = Color(0xff00c4b1),
            background = Color.White
        ),
        shapes = MaterialTheme.shapes.copy(
            extraSmall = RoundedCornerShape(10.dp),
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(10.dp),
            large = RoundedCornerShape(10.dp),
            extraLarge = RoundedCornerShape(10.dp),
        ),
        content = content
    )
}