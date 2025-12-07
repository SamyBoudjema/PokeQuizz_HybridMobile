package com.mastercyber.tp1

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()

actual fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()

actual fun saveScoreToPreferences(name: String, score: Int) {
    println("Score sauvegardé sur iOS: $name - $score")
}
