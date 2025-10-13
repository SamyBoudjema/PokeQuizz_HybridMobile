package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val hp: Int? = null,
    val atk: Int? = null,
    val def: Int? = null,
    val spe_atk: Int? = null,
    val spe_def: Int? = null,
    val vit: Int? = null
)

