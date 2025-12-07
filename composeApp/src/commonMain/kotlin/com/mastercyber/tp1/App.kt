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
import com.mastercyber.tp1.ui.screens.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var showQuiz by remember { mutableStateOf(false) }
        var showRanking by remember { mutableStateOf(false) }

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
        var quizQuestions by remember { mutableStateOf<List<Triple<Pokemon?, ByteArray?, ByteArray?>>>(emptyList()) }
        var currentQuestionIndex by remember { mutableStateOf(0) }
        var quizScore by remember { mutableStateOf(0) }

        var showNameDialog by remember { mutableStateOf(false) }
        var playerNameInput by remember { mutableStateOf("") }

        var leaderboard by remember { mutableStateOf(listOf<Triple<String, Int, String>>()) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        val resetContent: () -> Unit = {
            pokemon = null
            colorImage = null
            bwImage = null
            showBlackAndWhite = false
            reloadTrigger = 0
        }

        val resetQuizState: () -> Unit = {
            quizPokemon = null
            quizImage = null
            quizAnswer = ""
            quizResult = null
            quizTrigger = 0
            quizQuestions = emptyList()
            currentQuestionIndex = 0
            quizScore = 0
        }

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
                    showRanking = false
                    if (!showContent) resetContent()
                }) { Text(if (showContent) "Fermer" else "Révision Pokémon") }

                Button(
                    onClick = {
                        showQuiz = !showQuiz
                        showContent = false
                        showRanking = false
                        if (!showQuiz) resetQuizState()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text(if (showQuiz) "Fermer le Quiz" else "🎯 Quiz Pokémon") }

                Button(onClick = { showRanking = true; showContent = false; showQuiz = false }) { Text("🏆 Classement") }

                AnimatedVisibility(showContent) {
                    LaunchedEffect(reloadTrigger) {
                        if (showContent && reloadTrigger >= 0) {
                            isLoading = true
                            errorMessage = null
                            try {
                                val (poke, colorBytes, bwBytes) = Greeting().fetchPokemonWithImage()
                                if (poke == null) {
                                    errorMessage = "⚠️ Impossible de charger le Pokémon. Vérifiez votre connexion."
                                } else {
                                    pokemon = poke
                                    colorImage = colorBytes?.let { convertBytesToImageBitmap(it) }
                                    bwImage = bwBytes?.let { convertBytesToBlackAndWhite(it) }
                                }
                            } catch (e: Exception) {
                                errorMessage = "⚠️ Erreur: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = errorMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Button(
                                    onClick = { 
                                        errorMessage = null
                                        reloadTrigger++
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Réessayer")
                                }
                            }
                        }
                    }

                    PokemonScreen(
                        isLoading = isLoading,
                        pokemon = pokemon,
                        colorImage = colorImage,
                        bwImage = bwImage,
                        showBlackAndWhite = showBlackAndWhite,
                        onToggleBlackAndWhite = { showBlackAndWhite = !showBlackAndWhite },
                        onReload = { resetContent() }
                    )
                }

                AnimatedVisibility(showQuiz) {
                    LaunchedEffect(quizTrigger) {
                        if (showQuiz && quizTrigger >= 0) {
                            quizLoading = true
                            resetQuizState()
                            try {
                                val list = mutableListOf<Triple<Pokemon?, ByteArray?, ByteArray?>>()
                                repeat(10) {
                                    val (poke, colorBytes, bwBytes) = Greeting().fetchPokemonWithImage()
                                    list.add(Triple(poke, colorBytes, bwBytes))
                                }
                                quizQuestions = list
                                val first = quizQuestions.getOrNull(0)
                                quizPokemon = first?.first
                                quizImage = when {
                                    first?.third != null -> convertBytesToBlackAndWhite(first.third!!)
                                    first?.second != null -> convertBytesToBlackAndWhite(first.second!!)
                                    else -> null
                                }
                            } catch (_: Exception) {
                                resetQuizState()
                            } finally {
                                quizLoading = false
                            }
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
                            val isCorrect = correctName == userAnswer
                            quizResult = isCorrect
                            if (isCorrect) quizScore++
                        },
                        onNext = {
                            if (currentQuestionIndex < quizQuestions.size - 1) {
                                currentQuestionIndex++
                                val next = quizQuestions.getOrNull(currentQuestionIndex)
                                quizPokemon = next?.first
                                quizImage = when {
                                    next?.third != null -> convertBytesToBlackAndWhite(next.third!!)
                                    next?.second != null -> convertBytesToBlackAndWhite(next.second!!)
                                    else -> null
                                }
                                quizAnswer = ""
                                quizResult = null
                            } else {
                                showQuiz = false
                                showNameDialog = true
                            }
                        }
                    )
                }

                if (showNameDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showNameDialog = false
                            playerNameInput = ""
                            showRanking = true
                        },
                        title = { Text("Enregistrer ton score") },
                        text = {
                            Column {
                                Text("Ton score: $quizScore / ${quizQuestions.size}")
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = playerNameInput,
                                    onValueChange = { playerNameInput = it },
                                    label = { Text("Pseudo") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                val name = if (playerNameInput.isBlank()) "Joueur" else playerNameInput.trim()
                                val dateStr = "now"
                                leaderboard = leaderboard + Triple(name, quizScore, dateStr)
                                
                                try {
                                    saveScoreToPreferences(name, quizScore)
                                } catch (e: Exception) {
                                    println("Impossible de sauvegarder le score: ${e.message}")
                                }
                                
                                showNameDialog = false
                                playerNameInput = ""
                                resetQuizState()
                                showRanking = true
                            }) { Text("Enregistrer") }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showNameDialog = false
                                playerNameInput = ""
                                showRanking = true
                            }) { Text("Annuler") }
                        }
                    )
                }

                AnimatedVisibility(showRanking) {
                    RankingScreen(leaderboard = leaderboard, onClose = { showRanking = false })
                }
            }
        }
    }
}

expect fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap
expect fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap
expect fun saveScoreToPreferences(name: String, score: Int)
