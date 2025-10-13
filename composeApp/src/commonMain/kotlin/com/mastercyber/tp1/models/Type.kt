package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Type(
    val name: String? = null,
    val image: String? = null
)

