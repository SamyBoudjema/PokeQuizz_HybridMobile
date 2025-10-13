package com.mastercyber.tp1.models

import kotlinx.serialization.Serializable

@Serializable
data class Name(
    val fr: String? = null,
    val en: String? = null,
    val jp: String? = null
)

