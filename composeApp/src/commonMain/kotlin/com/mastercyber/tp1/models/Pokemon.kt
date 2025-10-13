package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val pokedexId: Int? = null,
    val name: Name? = null,
    val sprites: Sprites? = null,
    val types: List<Type>? = null,
    val stats: Stats? = null,
    val resistances: List<Resistance>? = null
)

