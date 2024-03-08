package utils

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Creates [ImageBitmap] from this [ByteArray]
 */
expect fun ByteArray.toComposeImageBitmap(): ImageBitmap
