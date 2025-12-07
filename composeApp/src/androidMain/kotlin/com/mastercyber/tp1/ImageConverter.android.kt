package com.mastercyber.tp1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.Context

private var appContext: Context? = null

fun setAppContext(context: Context) {
    appContext = context
}

actual fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap {
    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    return bitmap.asImageBitmap()
}

actual fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap {
    val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    val bwBitmap = Bitmap.createBitmap(
        originalBitmap.width,
        originalBitmap.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bwBitmap)
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply {
            setSaturation(0f)
        })
    }

    canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
    return bwBitmap.asImageBitmap()
}

actual fun saveScoreToPreferences(name: String, score: Int) {
    appContext?.let { context ->
        val sharedPref = context.getSharedPreferences("pokequizz_leaderboard", Context.MODE_PRIVATE)
        val scoreEntry = "$name|$score"
        val scores = sharedPref.getStringSet("scores", mutableSetOf()) ?: mutableSetOf()
        scores.add(scoreEntry)
        sharedPref.edit().putStringSet("scores", scores).apply()
    }
}

