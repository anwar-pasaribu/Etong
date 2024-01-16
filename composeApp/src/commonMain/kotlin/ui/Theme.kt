package ui

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
            onPrimary = Color(0xff1f2020),
            background = Color.White,
            surface = Color.White
        ),
        shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(10.dp),
            large = RoundedCornerShape(10.dp)
        ),
        content = content
    )
}