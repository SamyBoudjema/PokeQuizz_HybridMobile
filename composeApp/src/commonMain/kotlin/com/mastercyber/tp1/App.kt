package com.mastercyber.tp1

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedMenuItem by remember { mutableStateOf("pokemon") }

        var showContent by remember { mutableStateOf(false) }
        var pokemon by remember { mutableStateOf<Pokemon?>(null) }
        var colorImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var bwImage by remember { mutableStateOf<ImageBitmap?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var showBlackAndWhite by remember { mutableStateOf(false) }
        var reloadTrigger by remember { mutableStateOf(0) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    selectedItem = selectedMenuItem,
                    onItemSelected = { item ->
                        selectedMenuItem = item
                        scope.launch { drawerState.close() }
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("🎮 PokéApp") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Text("☰", style = MaterialTheme.typography.headlineMedium)
                            }
                        },
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
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (selectedMenuItem) {
                        "pokemon" -> {
                            MainActionButton(
                                showContent = showContent,
                                onClick = {
                                    showContent = !showContent
                                    if (!showContent) {
                                        pokemon = null
                                        colorImage = null
                                        bwImage = null
                                        showBlackAndWhite = false
                                        reloadTrigger = 0
                                    }
                                }
                            )

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

                                PokemonContent(
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
                        }
                        "about" -> AboutScreen()
                        "favorites" -> FavoritesScreen()
                        "settings" -> SettingsScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            // Header du menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎮 PokéApp",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Items du menu
            MenuItem(
                emoji = "⭐",
                label = "Pokémon Aléatoire",
                selected = selectedItem == "pokemon",
                onClick = { onItemSelected("pokemon") }
            )

            MenuItem(
                emoji = "❤️",
                label = "Favoris",
                selected = selectedItem == "favorites",
                onClick = { onItemSelected("favorites") }
            )

            MenuItem(
                emoji = "⚙️",
                label = "Paramètres",
                selected = selectedItem == "settings",
                onClick = { onItemSelected("settings") }
            )

            MenuItem(
                emoji = "ℹ️",
                label = "À propos",
                selected = selectedItem == "about",
                onClick = { onItemSelected("about") }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Footer
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun MenuItem(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = { Text(emoji, style = MaterialTheme.typography.titleLarge) },
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ℹ️",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "À propos de PokéApp",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Application Kotlin Multiplatform pour découvrir des Pokémon aléatoires.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Développée avec ❤️ en Compose Multiplatform",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "🎮 Version 1.0.0",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FavoritesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💙",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aucun favori pour le moment",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Cette fonctionnalité sera bientôt disponible !",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚙️ Paramètres",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider()

                SettingItem(
                    emoji = "🌙",
                    title = "Mode sombre",
                    subtitle = "Bientôt disponible"
                )

                HorizontalDivider()

                SettingItem(
                    emoji = "🔔",
                    title = "Notifications",
                    subtitle = "Recevoir des alertes"
                )

                HorizontalDivider()

                SettingItem(
                    emoji = "🌍",
                    title = "Langue",
                    subtitle = "Français"
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    emoji: String,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MainActionButton(
    showContent: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
        Text(if (showContent) "Fermer" else "Afficher un Pokémon!")
    }
}

@Composable
private fun PokemonContent(
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
            LoadingState()
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

@Composable
private fun LoadingState() {
    CircularProgressIndicator()
    Spacer(modifier = Modifier.height(16.dp))
    Text("Chargement du Pokémon...")
}

@Composable
private fun PokemonCard(
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

expect fun convertBytesToImageBitmap(bytes: ByteArray): ImageBitmap
expect fun convertBytesToBlackAndWhite(bytes: ByteArray): ImageBitmap
