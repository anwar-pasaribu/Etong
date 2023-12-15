package ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EtongTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(
            primary = Color.Black
        ),
        shapes = MaterialTheme.shapes.copy(
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(10.dp),
            large = RoundedCornerShape(10.dp)
        ),
        content = content
    )
}