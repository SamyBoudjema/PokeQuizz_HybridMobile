package com.mastercyber.tp1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.mastercyber.tp1.models.Pokemon
import com.mastercyber.tp1.ui.components.PokemonCard
import com.mastercyber.tp1.ui.components.LoadingState

@Composable
fun PokemonScreen(
    isLoading: Boolean,
    pokemon: Pokemon?,
    colorImage: ImageBitmap?,
    bwImage: ImageBitmap?,
    showBlackAndWhite: Boolean,
    onToggleBlackAndWhite: () -> Unit,
    onReload: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (isLoading) {
            LoadingState(text = "Chargement du Pokémon...")
        } else {
            pokemon?.let {
                PokemonCard(
                    pokemon = it,
                    colorImage = colorImage,
                    bwImage = bwImage,
                    showBlackAndWhite = showBlackAndWhite,
                    onToggleBlackAndWhite = onToggleBlackAndWhite,
                    onReload = onReload
                )
            }
        }
    }
}

