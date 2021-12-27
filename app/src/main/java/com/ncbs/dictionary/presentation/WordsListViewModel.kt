package com.ncbs.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncbs.dictionary.domain.DictionaryInteractor
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WordsListViewModel : ViewModel() {

    private val interactor = DictionaryInteractor()

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(emptyList())
    val words: StateFlow<List<Word>>
        get() = _words

    val isPlayButtonVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val selectedLocale: MutableStateFlow<Language> = MutableStateFlow(Language.NIVKH)

    init {
        selectedLocale.value = Language.NIVKH
        viewModelScope.launch {
            try {
                _words.value = interactor.getWords()
            } catch (e: Exception) {
                // TODO: 12/23/2021
            }
        }
    }

    fun getTranslateBySelectedLocale(word: Word?): String? {
        word ?: return null
        return word.locales[selectedLocale.value.code]?.value
    }

    fun onSearchQueryChanged(query: String? = "") {
        query ?: return

    }

    fun onPlay() {

    }
}