package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView(modifier: Modifier = Modifier, skeletonLayout: @Composable () -> Unit = {}) {
    Box(modifier = modifier.then(
        Modifier
            .width(24.dp)
            .height(24.dp))) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
        )
    }
}