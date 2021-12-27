package com.ncbs.dictionary.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ncbs.dictionary.databinding.ListItemWordBinding
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word

class WordsAdapter : RecyclerView.Adapter<WordViewHolder>() {

    private var list: List<Word> = emptyList()
    private var currentLanguage: Language = Language.NIVKH

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder(ListItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(list[position], currentLanguage)
    }

    override fun getItemCount(): Int = list.size

    fun setData(words: List<Word>) {
        list = words
        notifyDataSetChanged()
    }

    fun setCurrentLanguage(currentLanguage: Language) {
        this.currentLanguage = currentLanguage
        notifyDataSetChanged()
    }
}

class WordViewHolder(private val viewBinding: ListItemWordBinding) : RecyclerView.ViewHolder(viewBinding.title) {

    fun bind(word: Word, currentLanguage: Language) {
        viewBinding.title.text = word.getTranslate(currentLanguage.code)
    }
}