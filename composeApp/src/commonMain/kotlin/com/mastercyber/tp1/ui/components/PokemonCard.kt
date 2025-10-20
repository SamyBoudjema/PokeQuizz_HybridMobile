package com.mastercyber.tp1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mastercyber.tp1.models.Pokemon

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    colorImage: ImageBitmap?,
    bwImage: ImageBitmap?,
    showBlackAndWhite: Boolean,
    onToggleBlackAndWhite: () -> Unit,
    onReload: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PokemonHeader(pokemon)
            Spacer(modifier = Modifier.height(16.dp))
            PokemonImage(colorImage, bwImage, showBlackAndWhite)
            Spacer(modifier = Modifier.height(16.dp))
            ImageToggleButton(showBlackAndWhite, onToggleBlackAndWhite)
            Spacer(modifier = Modifier.height(16.dp))
            PokemonTypes(pokemon.types)
            PokemonStats(pokemon.stats)
            Spacer(modifier = Modifier.height(16.dp))
            ReloadButton(onReload)
        }
    }
}

@Composable
private fun PokemonHeader(pokemon: Pokemon) {
    Text(
        text = pokemon.name?.fr ?: "Inconnu",
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "N° ${pokemon.pokedexId ?: "???"}",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun PokemonImage(
    colorImage: ImageBitmap?,
    bwImage: ImageBitmap?,
    showBlackAndWhite: Boolean
) {
    val imageToShow = if (showBlackAndWhite) bwImage else colorImage
    imageToShow?.let { image ->
        Image(
            bitmap = image,
            contentDescription = "Pokemon",
            modifier = Modifier.size(250.dp)
        )
    }
}

@Composable
private fun ImageToggleButton(
    showBlackAndWhite: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(if (showBlackAndWhite) "Afficher en couleur" else "Afficher en noir et blanc")
    }
}

@Composable
private fun PokemonTypes(types: List<com.mastercyber.tp1.models.Type>?) {
    if (!types.isNullOrEmpty()) {
        Text(
            text = "Types:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            types.forEach { type ->
                TypeBadge(type.name ?: "")
            }
        }
    }
}

@Composable
private fun TypeBadge(typeName: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(
            text = typeName,
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PokemonStats(stats: com.mastercyber.tp1.models.Stats?) {
    stats?.let {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Statistiques:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            it.hp?.let { hp -> StatRow("❤️ PV", hp) }
            it.atk?.let { atk -> StatRow("⚔️ Attaque", atk) }
            it.def?.let { def -> StatRow("🛡️ Défense", def) }
            it.spe_atk?.let { speAtk -> StatRow("✨ Att. Spé", speAtk) }
            it.spe_def?.let { speDef -> StatRow("💫 Déf. Spé", speDef) }
            it.vit?.let { vit -> StatRow("⚡ Vitesse", vit) }
        }
    }
}

@Composable
private fun StatRow(label: String, value: Int) {
    Text("$label: $value")
}

@Composable
private fun ReloadButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Charger un autre Pokémon")
    }
}

