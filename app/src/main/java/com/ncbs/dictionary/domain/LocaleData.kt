package com.ncbs.dictionary.domain

import kotlinx.serialization.Serializable

@Serializable
data class LocaleData(
    val language: Language,
    val value: String,
    val audioPath: String? = null
)