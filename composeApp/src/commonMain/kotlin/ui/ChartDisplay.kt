package ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

@Composable
fun PaidAmountView(modifier: Modifier = Modifier, amount: Double, paid: Double) {
    Column(
        modifier = modifier.then(Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background
            )
        )
    ) {
        val paidPercentage = (paid/amount) * 100
        GradientProgressbar(progressValue = paidPercentage.toFloat())
    }
}

@Composable
fun GradientProgressbar(
    progressValue: Float = 0F,
    indicatorHeight: Dp = 24.dp,
    backgroundIndicatorColor: Color = Color.DarkGray.copy(alpha = 0.3f),
    gradientColors: List<Color> = listOf(
        Color(0xFFff7600),
        Color(0xFF15be53),
        Color(0xFF15be53),
        Color(0xFF15be53)
    ),
    animationDuration: Int = 1000,
    animationDelay: Int = 0
) {

    val rainbowColors = listOf(
        Color(0xff9c4f96),
        Color(0xffff6355),
        Color(0xfffba949),
        Color(0xfffae442),
        Color(0xff8bd448),
        Color(0xff2aa8f2)
    )

    val animateNumber = animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(
            durationMillis = animationDuration,
            delayMillis = animationDelay
        ), label = ""
    )

    val textMeasure = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(indicatorHeight)
    ) {

        val radius = size.height / 4f
        // Background indicator
        drawRoundRect(
            color = backgroundIndicatorColor,
            topLeft = Offset(x = 0f, y = 0f),
            cornerRadius = CornerRadius(radius),
            size = Size(width = size.width, height = size.height)
        )

        // Convert the downloaded percentage into progress (width of foreground indicator)
        val progress =
            (animateNumber.value / 100) * size.width // size.width returns the width of the canvas

        // Foreground indicator
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = if (progressValue >= 100) rainbowColors else gradientColors
            ),
            topLeft = Offset(x = 0f, y = 0f),
            cornerRadius = CornerRadius(radius),
            size = Size(width = progress.takeIf { it <= size.width } ?: size.width, height = size.height)
        )

        drawText(
            textMeasurer = textMeasure,
            text = "${ceil(progressValue.toDouble()).toInt()}%",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            ),
            topLeft = Offset(10.dp.toPx(), 4.dp.toPx())
        )
    }
}