package com.ncbs.dictionary.domain

import kotlinx.serialization.Serializable

@Serializable
data class Word(
    val id: String,
    val locales: Map<String, LocaleData>
) {

    fun getTranslate(languageCode: String): String? =
        locales[languageCode]?.value
}