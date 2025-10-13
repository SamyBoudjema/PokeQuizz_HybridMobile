package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Sprites(
    val regular: String? = null,
    val shiny: String? = null,
    val gmax: String? = null
)

