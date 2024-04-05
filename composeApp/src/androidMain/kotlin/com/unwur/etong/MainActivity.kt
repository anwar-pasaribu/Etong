package com.unwur.etong

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.initialize
import getScreenSizeInfo
import ui.extension.bouncingClickable
import ui.theme.EtongTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Firebase.initialize(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = getColor(R.color.etongPrimary),
                darkScrim = getColor(R.color.etongPrimaryDark)
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddButtonPreview() {
    EtongTheme {
        Row {
            AddButton {
                Text(text = "bbbb")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddButton(
    content: @Composable () -> Unit
) {

    val screenSizes = getScreenSizeInfo().wDP
    var isPressed by remember { mutableStateOf(false) }

    val animationTransition = updateTransition(isPressed, label = "ScalingBoxTransition")
    val roundedCornerAnimationVal by animationTransition.animateDp(
        targetValueByState = { pressed -> if (pressed) 16.dp else 28.dp },
        label = "RoundedCornerTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
            )
        }
    )

    val sizeWidthAnimation by animationTransition.animateDp(
        targetValueByState = { pressed ->
            if (pressed) {
                screenSizes.div(1.25f)
            } else {
                56.dp
            }
        },
        label = "SizeWidthTransition",
        transitionSpec = {
            spring(
            )
        }
    )

    val sizeHeightAnimation by animationTransition.animateDp(
        targetValueByState = { pressed ->
            if (pressed) {
                screenSizes.div(1.25f)
            } else {
                56.dp
            }
        },
        label = "SizeHeightTransition",
        transitionSpec = {
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy
            )
        }
    )

    val offsetAnimationVal by animationTransition.animateDp(
        targetValueByState = { pressed ->
            if (pressed) {
                0.dp
            } else {
                0.dp
            }
        },
        label = "OffsetYTransition",
    )


    BoxWithConstraints(
        modifier = Modifier
            .imeNestedScroll()
            .clickable(enabled = isPressed) {
                if (isPressed) {
                    isPressed = !isPressed
                }
            }
            .background(MaterialTheme.colorScheme.background.copy(alpha = .5f))
            .border(2.dp, Color.Red)
    ) {
        // Constraints H: ${constraints.maxHeight}, W: ${constraints.maxWidth},
        Box(
            modifier = Modifier
                .offset(x = 0.dp, offsetAnimationVal)
                .bouncingClickable { isPressed = !isPressed }
                .clip(RoundedCornerShape(roundedCornerAnimationVal))
                .size(width = sizeWidthAnimation, height = sizeHeightAnimation)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.TopCenter
        ) {
            Crossfade(
                targetState = isPressed,
                label = "ContentPreviewTransition"
            ) {
                if (it) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        content()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Icon(
                            modifier = Modifier
                                .graphicsLayer {
                                    alpha = if (it) 0f else 1f
                                }
                                .padding(10.dp)
                                .size(56.dp),
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                        )
                    }
                }
            }
        }
    }
}