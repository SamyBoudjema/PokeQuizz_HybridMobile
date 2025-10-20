package com.mastercyber.tp1

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mastercyber.tp1.models.Pokemon
import com.mastercyber.tp1.ui.screens.PokemonScreen
import com.mastercyber.tp1.ui.screens.QuizScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var showQuiz by remember { mutableStateOf(false) }

        var pokemon by remember { mutableStateOf<Pokemon?>(null) }
        var colorImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var bwImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var showBlackAndWhite by remember { mutableStateOf(false) }
        var reloadTrigger by remember { mutableStateOf(0) }

        var quizPokemon by remember { mutableStateOf<Pokemon?>(null) }
        var quizImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var quizAnswer by remember { mutableStateOf("") }
        var quizResult by remember { mutableStateOf<Boolean?>(null) }
        var quizLoading by remember { mutableStateOf(false) }
        var quizTrigger by remember { mutableStateOf(0) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("🎮 PokéApp") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = {
                    showContent = !showContent
                    showQuiz = false
                    if (!showContent) {
                        pokemon = null
                        colorImage = null
                        bwImage = null
                        showBlackAndWhite = false
                        reloadTrigger = 0
                    }
                }) {
                    Text(if (showContent) "Fermer" else "Révision Pokémon")
                }

                Button(
                    onClick = {
                        showQuiz = !showQuiz
                        showContent = false
                        if (!showQuiz) {
                            quizPokemon = null
                            quizImage = null
                            quizAnswer = ""
                            quizResult = null
                            quizTrigger = 0
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(if (showQuiz) "Fermer le Quiz" else "🎯 Quiz Pokémon")
                }

                AnimatedVisibility(showContent) {
                    LaunchedEffect(reloadTrigger) {
                        if (showContent && reloadTrigger >= 0) {
                            isLoading = true
                            val (poke, colorBytes, bwBytes) = Greeting().fetchPokemonWithImage()
                            pokemon = poke
                            colorImage = colorBytes?.let { convertBytesToImageBitmap(it) }
                            bwImage = bwBytes?.let { convertBytesToBlackAndWhite(it) }
                            isLoading = false
                        }
                    }

                    PokemonScreen(
                        isLoading = isLoading,
                        pokemon = pokemon,
                        colorImage = colorImage,
                        bwImage = bwImage,
                        showBlackAndWhite = showBlackAndWhite,
                        onToggleBlackAndWhite = { showBlackAndWhite = !showBlackAndWhite },
                        onReload = {
                            pokemon = null
                            colorImage = null
                            bwImage = null
                            showBlackAndWhite = false
                            reloadTrigger++
                        }
                    )
                }

                AnimatedVisibility(showQuiz) {
                    LaunchedEffect(quizTrigger) {
                        if (showQuiz && quizTrigger >= 0) {
                            quizLoading = true
                            quizAnswer = ""
                            quizResult = null
                            val (poke, colorBytes, _) = Greeting().fetchPokemonWithImage()
                            quizPokemon = poke
                            quizImage = colorBytes?.let { convertBytesToImageBitmap(it) }
                            quizLoading = false
                        }
                    }

                    QuizScreen(
                        isLoading = quizLoading,
                        pokemon = quizPokemon,
                        image = quizImage,
                        answer = quizAnswer,
                        result = quizResult,
                        onAnswerChange = { quizAnswer = it },
                        onSubmit = {
                            val correctName = quizPokemon?.name?.fr?.lowercase()?.trim()
                            val userAnswer = quizAnswer.lowercase().trim()
                            quizResult = correctName == userAnswer
                        },
                        onNext = {
                            quizPokemon = null
                            quizImage = null
                            quizAnswer = ""
                            quizResult = null
                            quizTrigger++
                        }
                    )
                }
            }
        }
    }
}

expect fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap
expect fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap
