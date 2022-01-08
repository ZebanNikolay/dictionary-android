package com.ncbs.dictionary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncbs.dictionary.R
import com.ncbs.dictionary.domain.DictionaryInteractor
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordsListViewModel : ViewModel() {

    private val interactor = DictionaryInteractor()

    private val query: MutableStateFlow<String> = MutableStateFlow("")

    private val _words: MutableStateFlow<List<Word>> = MutableStateFlow(emptyList())
    val words: StateFlow<List<Word>> = combine(query, _words) { query, words ->
        if (query.isBlank()) return@combine words
        words.filter { word -> word.locales.values.any { it.value.contains(query) } }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val isUpdateDialogShowing: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val selectedLocale: MutableStateFlow<Language> = MutableStateFlow(Language.NIVKH)
    val errorMessage: MutableSharedFlow<Int> = MutableSharedFlow(extraBufferCapacity = 1)

    init {
        selectedLocale.value = Language.NIVKH
        viewModelScope.launch {
            isUpdateDialogShowing.value = true
            try {
                _words.value = interactor.getWords()
            } catch (e: Exception) {
                errorMessage.tryEmit(R.string.error_message)
            }
            isUpdateDialogShowing.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        this.query.value = query
    }

    fun updateWords() {
        viewModelScope.launch {
            isUpdateDialogShowing.value = true
            try {
                _words.value = interactor.updateWords()
            } catch (e: Exception) {
                errorMessage.tryEmit(R.string.error_message)
            }
            isUpdateDialogShowing.value = false
        }
    }
}