package com.ncbs.dictionary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ncbs.dictionary.databinding.BottomSheetWordDetailsBinding
import com.ncbs.dictionary.domain.Language
import com.ncbs.dictionary.domain.Word

class WordDetailsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWordDetailsBinding? = null
    private val binding get() = _binding!!

    var word: Word? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWordDetailsBinding.inflate(inflater, container, false)
        binding.nvWord.text = word?.getTranslate(Language.NIVKH.code)
        binding.ruWord.text = word?.getTranslate(Language.RUSSIAN.code)
        binding.enWord.text = word?.getTranslate(Language.ENGLISH.code)
        return binding.root
    }
}