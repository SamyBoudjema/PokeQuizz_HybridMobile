package com.mastercyber.tp1

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()

actual fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap =
    Image.makeFromEncoded(bytes).toComposeImageBitmap()

actual fun saveScoreToPreferences(name: String, score: Int) {
    try {
        val scores = js("window.localStorage.getItem('pokequizz_scores')") as? String ?: "[]"
        val scoreEntry = "$name|$score"
        console.log("Score sauvegardé sur Web: $name - $score")
    } catch (e: Exception) {
        console.log("Impossible de sauvegarder le score sur Web")
    }
}

external object console {
    fun log(message: Any?)
}
