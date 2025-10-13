package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Resistance(
    val name: String? = null,
    val multiplier: Double? = null
)

