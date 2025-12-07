package com.mastercyber.tp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        initLeaderboardPreferences(this)

        setContent {
            App()
        }
    }
}

/** Initialise les SharedPreferences pour la persistance du classement */
fun initLeaderboardPreferences(context: android.content.Context) {
    val sharedPref = context.getSharedPreferences("pokequizz_leaderboard", android.content.Context.MODE_PRIVATE)
    if (!sharedPref.contains("initialized")) {
        sharedPref.edit().putBoolean("initialized", true).apply()
        val scores = sharedPref.getStringSet("scores", emptySet()) ?: emptySet()
        if (scores.isEmpty()) {
            val defaultScores = mutableSetOf(
                "Champion|100",
                "Maître Pokémon|95",
                "Entraîneur|85"
            )
            sharedPref.edit().putStringSet("scores", defaultScores).apply()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}