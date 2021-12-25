package com.ncbs.dictionary.domain

import kotlinx.serialization.Serializable

@Serializable
enum class Language(val title: String, val code: String) {
    NIVKH("Нивхский", "nv"),
    RUSSIAN("Русский", "ru"),
    ENGLISH("Английский", "en")
}