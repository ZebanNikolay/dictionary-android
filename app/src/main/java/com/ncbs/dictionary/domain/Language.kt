package com.ncbs.dictionary.domain

import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import com.ncbs.dictionary.R
import kotlinx.serialization.Serializable

@Serializable
enum class Language(val title: String, val code: String, @IdRes val menuId: Int) {
    NIVKH("Нивхский", "nv", R.id.nv_language_action),
    RUSSIAN("Русский", "ru", R.id.ru_language_action),
    ENGLISH("Английский", "en", R.id.en_language_action)
}