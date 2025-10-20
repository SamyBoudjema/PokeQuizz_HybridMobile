package com.mastercyber.tp1.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mastercyber.tp1.models.Pokemon
import com.mastercyber.tp1.ui.components.LoadingState
import com.mastercyber.tp1.ui.components.QuizResultCard

@Composable
fun QuizScreen(
    isLoading: Boolean,
    pokemon: Pokemon?,
    image: ImageBitmap?,
    answer: String,
    result: Boolean?,
    onAnswerChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isLoading) {
            LoadingState(text = "Chargement du Quiz...")
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Qui est ce Pokémon ?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    image?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Pokemon mystère",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    OutlinedTextField(
                        value = answer,
                        onValueChange = onAnswerChange,
                        label = { Text("Nom du Pokémon") },
                        enabled = result == null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    result?.let { isCorrect ->
                        QuizResultCard(
                            isCorrect = isCorrect,
                            pokemonName = pokemon?.name?.fr ?: "inconnu"
                        )
                    }

                    if (result == null) {
                        Button(
                            onClick = onSubmit,
                            enabled = answer.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Valider")
                        }
                    } else {
                        Button(
                            onClick = onNext,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Suivant ➡️")
                        }
                    }
                }
            }
        }
    }
}

