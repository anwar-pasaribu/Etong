package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import config.PlatformType
import getPlatform

@Composable
fun BackButton(modifier: Modifier = Modifier, onAction: () -> Unit) {
    Row(
        modifier = Modifier.clip(CircleShape).size(32.dp, 32.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            modifier = Modifier.fillMaxSize(),
            onClick = { onAction() },
            content = {
                Icon(
                    imageVector =
                        if (getPlatform().type == PlatformType.ANDROID)
                            Icons.AutoMirrored.Filled.ArrowBack
                        else Icons.Filled.ArrowBackIosNew,
                    contentDescription = null,
                )
            }
        )
    }

}