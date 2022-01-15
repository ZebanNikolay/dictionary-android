package com.ncbs.dictionary.data

import kotlinx.serialization.Serializable

@Serializable
data class WordDto(
    val id: String? = null,
    val nv: String? = null,
    val ru: String? = null,
    val en: String? = null
)