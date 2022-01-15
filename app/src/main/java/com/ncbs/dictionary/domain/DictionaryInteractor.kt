package com.ncbs.dictionary.domain

import com.ncbs.dictionary.data.DictionaryRepository

class DictionaryInteractor(
    private val repository: DictionaryRepository = DictionaryRepository()
) {

    suspend fun getWords(): List<Word> = repository.getWords()

    suspend fun updateWords(): List<Word> = repository.updateWords()

    suspend fun isUrlExist(url: String?): Boolean = repository.isUrlExist(url)
}